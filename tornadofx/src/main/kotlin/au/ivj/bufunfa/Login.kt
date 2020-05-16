package au.ivj.bufunfa

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import tornadofx.*


class LoginView : View() {
    val graphQLService: GraphQLService by di()

    override val root = Form()
    private val model = Model()

    class Model {
        var userName: String by property<String>()
        fun userNameProperty() = getProperty(Model::userName)

        var password: String by property<String>()
        fun passwordProperty() = getProperty(Model::password)

        var connectJob: Job? by property<Job>()
        fun connectJobProperty() = getProperty(Model::connectJob)
    }

    fun tryConnect() {
        model.connectJob = GlobalScope.launch(Dispatchers.IO) {
            if (graphQLService.tryConnect("https://localhost:8443/graphql", model.userName, model.password)) {
                GlobalScope.launch(Dispatchers.JavaFx) {
                    replaceWith<DashboardView>()
                }
            }
            GlobalScope.launch(Dispatchers.JavaFx) {
                model.connectJob = null
            }
        }
    }

    fun cancelConnect() {
        model.connectJob?.cancel()
        model.connectJob = null
    }

    init {
        with(root) {
            fieldset("Enter credentials") {
                field("User") {
                    textfield().bind(model.userNameProperty())
                }
                field("Password") {
                    passwordfield().bind(model.passwordProperty())
                }
            }
            button("Login") {
                setOnAction {
                    if (model.connectJob == null) {
                        tryConnect()
                    } else {
                        cancelConnect()
                    }
                }
                disableProperty().bind(model.userNameProperty().isNull)
                textProperty().bind(model.connectJobProperty().isNull.stringBinding { if (it === true) "Login" else "Cancel" })
            }
        }
    }
}