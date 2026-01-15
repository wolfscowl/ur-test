package com.wolfscowl.ur_client.test

import kotlin.reflect.KClass


// Gemeinsames Interface für alle Werkzeuge
sealed interface RobotTool {
    val robot: ToolAction
    val model: String
    val toolIndex: Int
}


// Greifer-Werkzeug mit eigenen Methoden
data class Gripper(override val robot: ToolAction, override val model: String, override val toolIndex: Int) : RobotTool {
    fun grip() { robot.sendData("rpcxml.grip()") }
    fun release() { robot.sendData("rpcxml.release()") }
}

// Schraubendreher-Werkzeug mit eigenen Methoden
data class Screwdriver(override val robot: ToolAction, override val model: String, override val toolIndex: Int) : RobotTool {
    fun turnLeft() { robot.sendData("rpcxml.left()") }
    fun turnRight() { robot.sendData("rpcxml.right()") }
}


// Gemeinsames Interface für alle Werkzeuge
sealed interface ToolAction {
    fun sendData(script: String)
}


// Roboterklasse mit generischer Factory-Methode
class Robot(val host: String): ToolAction {
    var tool: RobotTool? = null
    var toolType: KClass<out RobotTool> = Gripper::class

    override fun sendData(script: String) {
        println("Send script to $host: $script")
    }

    fun attachTool(robotTool: RobotTool) {
        tool = robotTool
        toolType = robotTool::class
    }

    inline fun <reified T : RobotTool> getTooly(): T? {
        return tool as? T
    }
}


//
//// Hauptprogramm
fun main() {
    val robot = Robot("192.168.1.1")

    val gripper = Gripper(robot, "Gripper-3000", 1)
    robot.attachTool(gripper)
    robot.getTooly<Gripper>()?.grip()
}