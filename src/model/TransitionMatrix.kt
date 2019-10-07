package model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.collections4.bidimap.DualHashBidiMap

class TransitionMatrix() {
    private lateinit var matrix: Array<Array<State?>>

    val alphabetIndexes = DualHashBidiMap<Alphabet, Int>()
    val stateIndexes = DualHashBidiMap<State, Int>()

    private suspend fun buildCollections(transitionFunctionCollection: TransitionFunctionCollection) =
        withContext(Dispatchers.Default) {
            for (i in 0 until transitionFunctionCollection.alphabetCollection.size)
                alphabetIndexes[transitionFunctionCollection.alphabetCollection[i]] = i

            for (i in 0 until transitionFunctionCollection.stateCollection.size)
                stateIndexes[transitionFunctionCollection.stateCollection[i]] = i
        }

    private suspend fun buildMatrix(transitionFunctionCollection: TransitionFunctionCollection) =
        withContext(Dispatchers.Default) {
            matrix = Array(transitionFunctionCollection.stateCollection.size) {
                arrayOfNulls<State?>(transitionFunctionCollection.alphabetCollection.size)
            }

            for (i in transitionFunctionCollection) {
                matrix[stateIndexes[i.fromState]!!][alphabetIndexes[i.alphabet]!!] = i.toState
            }
        }

    suspend fun fromTransitionFunctionCollection(transitionFunctionCollection: TransitionFunctionCollection): TransitionMatrix {
        buildCollections(transitionFunctionCollection)
        buildMatrix(transitionFunctionCollection)

        return this
    }

    suspend fun toTransitionFunctionCollection() = withContext(Dispatchers.Default) {
        val transitionFunctionCollection = TransitionFunctionCollection()

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                matrix[i][j]?.let {
                    val fromState = stateIndexes.getKey(i)
                    val alphabet = alphabetIndexes.getKey(j)
                    transitionFunctionCollection.insert(TransitionFunction(fromState, alphabet, it))
                }
            }
        }

        return@withContext transitionFunctionCollection
    }

    override fun toString(): String {
        var returnValue = "|    |"
        for (i in alphabetIndexes.keys)
            returnValue += "   $i  |"

        for (i in matrix.indices) {
            returnValue += "\n|"
            for (j in -1 until matrix[i].size) {
                returnValue += if (j == -1)
                    " ${stateIndexes.getKey(i)} |"
                else {
                    val curr = matrix[i][j]
                    if (curr == null)
                        " ---- |"
                    else
                        "  ${matrix[i][j]}  |"

                }
            }
        }

        return returnValue
    }

    operator fun get(state: State, alphabet: Alphabet) =
        matrix[stateIndexes[state]!!][alphabetIndexes[alphabet]!!]
}