package model

class TransitionMatrix(val transitionFunctionCollection: TransitionFunctionCollection) {
    private lateinit var matrix: Array<Array<State?>>

    val alphabetIndexes = HashMap<Alphabet, Int>()
    val stateIndexes = HashMap<State, Int>()

    init {
        buildCollections()
        buildMatrix()
    }

    private fun buildCollections() {
        for (i in 0 until transitionFunctionCollection.alphabetCollection.size)
            alphabetIndexes[transitionFunctionCollection.alphabetCollection[i]] = i

        for (i in 0 until transitionFunctionCollection.stateCollection.size)
            stateIndexes[transitionFunctionCollection.stateCollection[i]] = i
    }

    private fun buildMatrix() {
        matrix = Array(transitionFunctionCollection.stateCollection.size) {
            arrayOfNulls<State?>(transitionFunctionCollection.alphabetCollection.size)
        }

        for (i in transitionFunctionCollection) {
            matrix[stateIndexes[i.fromState]!!][alphabetIndexes[i.alphabet]!!] = i.toState
        }
    }

    override fun toString(): String {
        var returnValue = "|    |"
        for (i in transitionFunctionCollection.alphabetCollection)
            returnValue += "   $i  |"

        for (i in matrix.indices) {
            returnValue += "\n|"
            for (j in -1 until matrix[i].size) {
                if (j == -1)
                    returnValue += " ${transitionFunctionCollection.stateCollection[i]} |"
                else {
                    val curr = matrix[i][j]
                    if (curr == null)
                        returnValue += " ---- |"
                    else
                        returnValue += "  ${matrix[i][j]}  |"

                }
            }
        }

        return returnValue
    }

    operator fun get(state: State, alphabet: Alphabet) =
        matrix[stateIndexes[state]!!][alphabetIndexes[alphabet]!!]
}