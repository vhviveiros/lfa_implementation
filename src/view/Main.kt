package view

import javafx.application.Application
import javafx.application.Application.launch
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun main() {
    launch()
}

class Main : Application(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.JavaFx

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("main.fxml"))

        launch {
            primaryStage.title = "Autômato"
            primaryStage.scene = Scene(root, 450.0, 400.0)
            primaryStage.isResizable = false
            primaryStage.show()
        }
    }
}
