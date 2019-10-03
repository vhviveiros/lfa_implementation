import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.*
import java.util.concurrent.CompletableFuture

fun main() = runBlocking<Unit> {
    val states = arrayOf(
        State(isInitial = true, isFinal = true),
        State(isFinal = true),
        State(isFinal = true),
        State()
    )
    val alphabet = arrayOf(Alphabet('0'), Alphabet('1'))

    val transitionCollection = TransitionFunctionCollection()
    transitionCollection.insert(TransitionFunction(states[0], alphabet[1], states[0]))
    transitionCollection.insert(TransitionFunction(states[0], alphabet[0], states[1]))
    transitionCollection.insert(TransitionFunction(states[1], alphabet[0], states[2]))
    transitionCollection.insert(TransitionFunction(states[1], alphabet[1], states[0]))
    transitionCollection.insert(TransitionFunction(states[2], alphabet[0], states[3]))
    transitionCollection.insert(TransitionFunction(states[2], alphabet[1], states[0]))
    transitionCollection.insert(TransitionFunction(states[3], alphabet[0], states[3]))
    transitionCollection.insert(TransitionFunction(states[3], alphabet[1], states[0]))

    val automaton = Automaton(transitionCollection)

//    println(automaton.toMatrix())
//    println(transitionCollection)
    launch {
        println(automaton.verifySentence("11100"))
    }
}