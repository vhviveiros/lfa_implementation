package model

typealias States = Array<State>

class State(var isInitial: Boolean = false, var isFinal: Boolean = false, val label: String = "q$count") {

    init {
        count++
    }

    companion object {
        var count = 0
    }

    override fun equals(other: Any?) = other is State && other.label == label

    override fun toString() = "${if (isInitial) "*" else ""}$label${if (isFinal) "*" else ""}"
}