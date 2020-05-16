package au.ivj.bufunfa

import javafx.scene.control.Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import tornadofx.*

/**
 * For anybody reading this code, please don't think I'd ever write a code like this in production :D. It was just the
 * way I found to squeeze as many concepts as posslible in one class.
 */
class LoginView : View() {
    override val root = Form()

    private val model = Model()
    private val graphQLService: GraphQLService by di() // First view needs to use DI, as it is loaded statically and we can't inject via Spring

    private var loginButton: Button by singleAssign() // Just keep a reference to change the text in a method

    class Model {
        var userName: String by property<String>()
        fun userNameProperty() = getProperty(Model::userName)

        var password: String by property<String>()
        fun passwordProperty() = getProperty(Model::password)

        var connectJob: Job? = null
    }

    fun tryConnect() {
        model.connectJob = GlobalScope.launch(Dispatchers.IO) {
            if (graphQLService.tryConnect("https://localhost:8443/graphql", model.userName, model.password)) {
                GlobalScope.launch(Dispatchers.JavaFx) {
                    replaceWith<DashboardView>()
                }
            }
            model.connectJob = null
            GlobalScope.launch(Dispatchers.JavaFx) {
                loginButton.text = "Login" // Has to happen in the UI thread. Before coroutines, this was runAsync{} ui {}
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
            loginButton = button("Login") {
                setOnAction {
                    if (text === "Login") { // Here we are already in the UI thread.
                        text = "Cancel"
                        tryConnect()
                    } else {
                        text = "Login"
                        cancelConnect()
                    }
                }
                disableProperty().bind(model.userNameProperty().isNull)
            }
        }
    }
}