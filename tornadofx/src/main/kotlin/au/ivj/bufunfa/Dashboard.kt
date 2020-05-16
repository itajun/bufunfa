package au.ivj.bufunfa

import tornadofx.*

class DashboardView : View() {
    override val root = Form()

    private class Model {
    }

    private val model = Model()

    init {
        primaryStage.isMaximized = true
        with(root) {
            label("XPTO")
        }
    }
}