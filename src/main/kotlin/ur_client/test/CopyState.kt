package com.wolfscowl.ur_client.test

import com.wolfscowl.ur_client.core.internal.state.MutableArmState
import com.wolfscowl.ur_client.model.element.JointPosition
import com.wolfscowl.ur_client.model.element.Pose
import com.wolfscowl.ur_client.model.element.RunningState

//data class Value1( var valueName: String = "Value1") {
//    var valueNumber: Int = 22
//
//    override fun toString(): String {
//        return "Value1(valueName: $valueName, valueNumber: $valueNumber)"
//    }
//}
//
//data class Object1(var name: String = "Object1",val value: Value1 = Value1())
//
//
//
//
//
//fun main() {
//
//    val object1 = Object1()
//    val object2 = object1.copy()
//
//    object1.name = "Peter"
//    object1.value.valueName = "Hans"
//    object1.value.valueNumber = 1111
//
//    println(object1)
//    println(object2)
//}

fun main() {

    val state = MutableArmState()
    state.scriptName = "Test1"
    state.runningState = RunningState.PENDING
    state.safetyModeStop = false
    state.jointPosition = JointPosition(1.0,1.0,1.0,1.0,1.0,1.0)
    state.tcpPose = Pose(1.0,1.0,1.0,1.0,1.0,1.0)
    val statecopy = state.copy()
    state.scriptName = "Test2"
    state.runningState = RunningState.START
    state.safetyModeStop = true
    state.jointPosition = JointPosition(2.0,2.0,2.0,2.0,2.0,2.0)
    state.tcpPose = Pose(2.0,2.0,2.0,2.0,2.0,2.0)

    println(state)
    println(statecopy)

}