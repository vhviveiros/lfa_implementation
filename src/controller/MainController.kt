package controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.text.Text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.*
import java.net.URL
import java.util.*

class MainController {
    private var fromState: State? = null
    private var toState: State? = null
    private var lastSelectedStateIndex = -1

    @FXML
    private lateinit var alphabetList: ListView<Alphabet>
    @FXML
    private lateinit var stateList: ListView<State>
    @FXML
    private lateinit var functionList: ListView<TransitionFunction>

    @FXML
    private lateinit var edtAlphabet: TextField;
    @FXML
    private lateinit var edtState: TextField;

    @FXML
    private lateinit var chkInitial: CheckBox
    @FXML
    private lateinit var chkFinal: CheckBox

    @FXML
    private lateinit var edtSentence: TextField
    @FXML
    private lateinit var btnVerify: Button
    @FXML
    private lateinit var result: Text

    @FXML
    lateinit var functionState: Text

    @FXML
    private val location: URL? = null

    @FXML
    private val resources: ResourceBundle? = null

    @FXML
    private fun initialize() = GlobalScope.launch(Dispatchers.JavaFx) {
        stateList.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            onStateSelected(newValue)
        };

        alphabetList.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            onAlphabetSelected(newValue)
        }

        stateList.focusedProperty().addListener { _, _, isFocused ->
            if (!isFocused) {
                lastSelectedStateIndex = stateList.selectionModel.selectedIndex
                stateList.selectionModel.clearSelection()
            }
        }

        btnVerify.focusedProperty().addListener { _, _, isFocused ->
            if (!isFocused)
                result.text = "      "
        }
    }

    private fun onAlphabetSelected(newValue: Alphabet?) {
        functionState.text =
            "σ(${fromState ?: ""}, ${newValue ?: ""}) = ${toState ?: ""}"
    }

    private fun onStateSelected(state: State?) = GlobalScope.launch(Dispatchers.JavaFx) {
        chkInitial.isSelected = state?.isInitial ?: false
        chkFinal.isSelected = state?.isFinal ?: false

        if (state != null)
            when {
                fromState == null -> fromState = state
                toState == null -> toState = state
                else -> {
                    fromState = state
                    toState = null
                }
            }

        functionState.text =
            "σ(${fromState ?: ""}, ${alphabetList.selectionModel.selectedItem ?: ""}) = ${toState ?: ""}"
    }

    @FXML
    fun onAddAlphabet(ae: ActionEvent) = GlobalScope.launch(Dispatchers.JavaFx) {
        val newValue = Alphabet(edtAlphabet.text[0])
        if (!alphabetList.items.contains(newValue))
            alphabetList.items.add(newValue)
        edtAlphabet.clear()
    }

    @FXML
    fun onAddState(ae: ActionEvent) = GlobalScope.launch(Dispatchers.JavaFx) {
        if (edtState.text.isNotEmpty()) {
            val newValue = State(label = edtState.text)
            if (!stateList.items.contains(newValue))
                stateList.items.add(newValue)
        }
        chkInitial.isSelected = false
        chkFinal.isSelected = false
        edtState.clear()
    }

    @FXML
    fun onAddFunction(ae: ActionEvent) = GlobalScope.launch(Dispatchers.JavaFx) {
        if (fromState != null && toState != null)
            if (!alphabetList.selectionModel.isEmpty) {
                val newValue = TransitionFunction(fromState!!, alphabetList.selectionModel.selectedItem, toState!!)
                if (!functionList.items.contains(newValue))
                    functionList.items.add(
                        TransitionFunction(fromState!!, alphabetList.selectionModel.selectedItem, toState!!)
                    )
            }
    }

    @FXML
    fun onRemoveAlphabet(me: MouseEvent) = GlobalScope.launch(Dispatchers.JavaFx) {
        if (me.clickCount == 2) {
            val selectedAlphabet = alphabetList.selectionModel.selectedItem
            alphabetList.items.remove(selectedAlphabet)
            functionList.items.removeIf { it.alphabet == selectedAlphabet }
        }
    }

    @FXML
    fun onRemoveFunction(me: MouseEvent) = GlobalScope.launch(Dispatchers.JavaFx) {
        if (me.clickCount == 2)
            functionList.items.remove(functionList.selectionModel.selectedItem)
    }

    @FXML
    fun onRemoveState(me: MouseEvent) = GlobalScope.launch(Dispatchers.JavaFx) {
        if (me.clickCount == 2) {
            val selectedState = stateList.selectionModel.selectedItem
            stateList.items.remove(selectedState)
            functionList.items.removeIf { it.fromState == selectedState || it.toState == selectedState }

            fromState = null
            toState = null
        }
    }

    private fun clearFunctionState() {
        functionState.text = "σ(, ) = "
    }

    @FXML
    fun onClear() {
        alphabetList.items.clear()
        stateList.items.clear()
        functionList.items.clear()
        clearFunctionState()
    }

    @FXML
    fun onSetIsInitial() = GlobalScope.launch(Dispatchers.JavaFx) {
        if (lastSelectedStateIndex != -1) {
            stateList.items[lastSelectedStateIndex].isInitial = !stateList.items[lastSelectedStateIndex].isInitial
            stateList.fireEvent(
                ListView.EditEvent<State>(
                    stateList,
                    ListView.editCommitEvent<State>(),
                    stateList.items[lastSelectedStateIndex],
                    lastSelectedStateIndex
                )
            )
        }
    }

    @FXML
    fun onSetIsFinal() = GlobalScope.launch(Dispatchers.JavaFx) {
        if (lastSelectedStateIndex != -1) {
            stateList.items[lastSelectedStateIndex].isFinal = !stateList.items[lastSelectedStateIndex].isFinal
            stateList.fireEvent(
                ListView.EditEvent<State>(
                    stateList,
                    ListView.editCommitEvent<State>(),
                    stateList.items[lastSelectedStateIndex],
                    lastSelectedStateIndex
                )
            )
        }
    }

    @FXML
    fun onVerify() = GlobalScope.launch(Dispatchers.JavaFx) {
        if (edtSentence.text.isNotEmpty() && !functionList.items.isEmpty()) {
            val automaton = Automaton(TransitionFunctionCollection(functionList.items))
            val isAccepted = withContext(Dispatchers.Default) {
                automaton.verifySentence(edtSentence.text)
            }

            if (isAccepted) {
                result.text = "Válido!"
                result.fill = Color.GREEN
            } else {
                result.text = "Inválido!"
                result.fill = Color.RED
            }
        }
    }
}
