package model

import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.collections.ArrayList

class Automaton(val transitionFunctionCollection: TransitionFunctionCollection) {
    fun getInitialState() = transitionFunctionCollection.stateCollection.first { it.isInitial }

    fun getFinalStates() = transitionFunctionCollection.stateCollection.filter { it.isFinal }

    fun toMatrix() = transitionFunctionCollection.toMatrix()

    fun verifySentence(sentence: String): Boolean {
        //Convert string to queue, so we can pop()
        val queue = LinkedList<Char>()
        queue.addAll(sentence.toCharArray().toTypedArray())
        return verifySentence(queue, getInitialState())
    }

    private fun verifySentence(sentence: LinkedList<Char>, state: State): Boolean {
        if (sentence.isEmpty())
            return state.isFinal

        for (i in transitionFunctionCollection.next(state, Alphabet(sentence.pop())))
            return verifySentence(sentence, i)

        return false
    }
}