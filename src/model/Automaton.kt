package model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class Automaton(val transitionFunctionCollection: TransitionFunctionCollection) {
    fun getInitialState() = transitionFunctionCollection.stateCollection.first { it.isInitial }

    fun getFinalStates() = transitionFunctionCollection.stateCollection.filter { it.isFinal }

    suspend fun toMatrix() = transitionFunctionCollection.toMatrix()

    suspend fun verifySentence(sentence: String): Boolean = withContext(Dispatchers.Default) {
        //Convert string to queue, so we can pop()
        val queue = LinkedList<Char>()
        queue.addAll(sentence.toCharArray().toTypedArray())

        return@withContext verifySentence(queue, getInitialState())
    }

    private fun verifySentence(sentence: LinkedList<Char>, state: State): Boolean {
        if (sentence.isEmpty())
            return state.isFinal || transitionFunctionCollection.getVoidPaths(state).any { it.toState.isFinal }

        val char = sentence[0]
        val paths = transitionFunctionCollection.next(state, Alphabet(char))

        paths.forEach {
            val copy = LinkedList<Char>()
            copy.addAll(sentence)
            if (!it.alphabet.isVoid())
                copy.pop()
            if (verifySentence(copy, it.toState))
                return true
        }

        return false
    }
}