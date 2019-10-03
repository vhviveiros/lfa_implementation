package model

typealias States = Array<State>

class State(val isInitial: Boolean = false, val isFinal: Boolean = false, val label: String = "q$count") {

    init {
        count++
    }

    companion object {
        var count = 0
    }

    override fun toString() = label
}