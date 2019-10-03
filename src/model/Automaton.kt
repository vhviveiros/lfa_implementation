package model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class Automaton(val transitionFunctionCollection: TransitionFunctionCollection) {
    fun getInitialState() = transitionFunctionCollection.stateCollection.first { it.isInitial }

    fun getFinalStates() = transitionFunctionCollection.stateCollection.filter { it.isFinal }

    fun toMatrix() = transitionFunctionCollection.toMatrix()

    suspend fun verifySentence(sentence: String): Boolean = withContext(Dispatchers.Default) {
        //Convert string to queue, so we can pop()
        val queue = LinkedList<Char>()
        queue.addAll(sentence.toCharArray().toTypedArray())

        return@withContext verifySentence(queue, getInitialState())
    }

    private suspend fun verifySentence(sentence: LinkedList<Char>, state: State): Boolean =
        withContext(Dispatchers.Default) {
            if (sentence.isEmpty())
                return@withContext state.isFinal

            for (i in transitionFunctionCollection.next(state, Alphabet(sentence.pop())))
                return@withContext verifySentence(sentence, i)

            return@withContext false
        }
}