package com.wolfscowl.ur_client.test.cmdThread



//class RobotArm(val robot: Robot) {
//    fun move(x: Int,y: Int,z: Int) {
//        robot.sendCmd("move to $x $y $z")
//    }
//}
//
//class Gripper(val robot: Robot) {
//    fun release(width: Int) {
//        robot.sendCmd("open gripper $width")
//    }
//    fun grip(width: Int) {
//        robot.sendCmd("close gripper $width")
//    }
//}
//
//
//class Robot() {
//    val arm = RobotArm(this)
//    val tool = Gripper(this)
//    fun sendCmd(cmd: String) {
//        println("CMD + $cmd")
//    }
//}
//
//
//fun main2() {
//    val robot = Robot()
//    robot.tool.grip(20)
//    robot.arm.move(2,3,5)
//
//    robot.thread{
//        robot.tool.grip(20)
//        robot.arm.move(2,3,5)
//    }.run()
//}
// =================================================================================================
// ================================== DeepSeek 02 Überarbeitet 02 ====================================
// =================================================================================================



// Schnittstellen für die Operationen
interface GripperOperations {
    fun grip(width: Int)
    fun release(width: Int)
}

interface ArmOperations {
    fun move(x: Int, y: Int, z: Int)
}

class RobotArm : ArmOperations {
    override fun move(x: Int, y: Int, z: Int) {
        println("move to $x $y $z")
    }
}

class Gripper : GripperOperations {
    override fun release(width: Int) {
        println("open gripper $width")
    }
    override fun grip(width: Int) {
        println("close gripper $width")
    }
}

class Robot {
    val arm = RobotArm()
    val tool = Gripper()

    fun thread(block: RobotCommandScope.() -> Unit): RobotThread {
        val scope = RobotCommandScope()
        scope.block()
        return RobotThread(scope.commands)
    }
}

class RobotCommandScope {
    val commands = mutableListOf<Robot.() -> Unit>()

    val tool: GripperOperations = object : GripperOperations {
        override fun grip(width: Int) {
            commands.add { tool.grip(width) }
        }
        override fun release(width: Int) {
            commands.add { tool.release(width) }
        }
    }

    val arm: ArmOperations = object : ArmOperations {
        override fun move(x: Int, y: Int, z: Int) {
            commands.add { arm.move(x, y, z) }
        }
    }
}

class RobotThread(private val commands: List<Robot.() -> Unit>) {
    fun run() {
        commands.forEach { it(Robot()) } // Hier sollte das echte Robot-Objekt verwendet werden
    }
}

fun main() {
    val robot = Robot()

    robot.thread {
        tool.grip(20)
        arm.move(2, 3, 5)
    }.run()

    {
        println("hello")
        println("huhu")
    }
}



// =================================================================================================
// ================================== DeepSeek 02 Überarbeitet 02 ====================================
// =================================================================================================



//// Schnittstellen für die Operationen
//interface GripperOperations {
//    fun grip(width: Int)
//    fun release(width: Int)
//}
//
//interface ArmOperations {
//    fun move(x: Int, y: Int, z: Int)
//}
//
//class RobotArm : ArmOperations {
//    override fun move(x: Int, y: Int, z: Int) {
//        println("move to $x $y $z")
//    }
//}
//
//class Gripper : GripperOperations {
//    override fun release(width: Int) {
//        println("open gripper $width")
//    }
//    override fun grip(width: Int) {
//        println("close gripper $width")
//    }
//}
//
//class Robot {
//    val arm = RobotArm()
//    val tool = Gripper()
//
//    fun thread(block: RobotCommandScope.() -> Unit): Thread {
//        val scope = RobotCommandScope(this)
//        scope.block()
//        return Thread {
//            scope.commands.forEach { this.it() }
//        }
//    }
//}
//
//class RobotCommandScope(robot: Robot) {
//    val commands = mutableListOf<Robot.()->Unit>()
//
//    // Verwende die Schnittstellen-Typen
//    val tool: GripperOperations = object : GripperOperations {
//        override fun grip(width: Int) {
//            commands.add({tool.grip(width)})
//        }
//        override fun release(width: Int) {
//            commands.add({tool.release(width)})
//        }
//    }
//
//    val arm: ArmOperations = object : ArmOperations {
//        override fun move(x: Int, y: Int, z: Int) {
//            commands.add({arm.move(x,y,z)})
//        }
//    }
//}
//
//
//
//fun main() {
//    val robot = Robot()
//
//    robot.thread {
//        tool.grip(20)    // Jetzt erkennbar als GripperOperations
//        arm.move(2, 3, 5) // Jetzt erkennbar als ArmOperations
//    }.start()
//}

// ==================================================================================================
// ========================================== OpenAi 01 ==============================================
// ==================================================================================================


//// Die Roboter-Klassen
//class RobotArm {
//    fun move(x: Int, y: Int, z: Int) {
//        println("move to $x $y $z")
//    }
//}
//
//class Gripper {
//    fun release(width: Int) {
//        println("open gripper $width")
//    }
//    fun grip(width: Int) {
//        println("close gripper $width")
//    }
//}
//
//class Robot {
//    val arm = RobotArm()
//    val tool = Gripper()
//}
//
//// Unsere DSL-Klasse für den „Thread“
//class RobotThread(private val robot: Robot) {
//    // Liste der Befehle, die jeweils mit dem Roboter als Kontext ausgeführt werden
//    private val commands = mutableListOf<Robot.() -> Unit>()
//
//    // Fügt einen Befehl zur Liste hinzu
//    fun command(action: Robot.() -> Unit) {
//        commands.add(action)
//    }
//
//    // DSL-Operator, um Befehle kompakter hinzuzufügen
//    operator fun (Robot.() -> Unit).unaryPlus() {
//        command(this)
//    }
//
//    // Führt alle gespeicherten Befehle in der Reihenfolge ihres Hinzufügens aus
//    fun run() {
//        for (action in commands) {
//            robot.action()
//        }
//    }
//}
//
//// Erweiterungsfunktion an Robot, die einen Thread erstellt
//fun Robot.thread(block: RobotThread.() -> Unit): RobotThread {
//    val thread = RobotThread(this)
//    thread.block()
//    return thread
//}
//
//
//// Beispielhafte Verwendung in main
//fun main() {
//    val robot = Robot()
//
//    // Direkte Ausführung:
//    robot.tool.grip(22)
//    robot.arm.move(2, 3, 5)
//
//    // Befehle in einem „Thread“ sammeln:
//    // Hier gibt es zwei Varianten:
//
//    // Variante 1: Mit explizitem "command"
//    robot.thread {
//        command { tool.grip(20) }
//        command { arm.move(2, 3, 5) }
//    }.run()
//
//    // Variante 2: Mit dem DSL-Operator, um den Code noch kompakter zu schreiben:
//    /*
//    robot.thread {
//        + { robot.tool.grip(20) }
//        + { robot.arm.move(2, 3, 5) }
//    }.run()
//    */
//}



// ==================================================================================================
// ========================================== DeepSeek 01 ==============================================
// ==================================================================================================

// 1. Definiere die verschiedenen Commands mit einer 'sealed class'
sealed class Command {
    data class MoveArm(val x: Double, val y: Double, val z: Double) : Command()
    data class Grib(val power: Int) : Command()
    data class MoveArmX(val x: Double) : Command()
}

// 2. Erstelle eine Klasse, die Methodenaufrufe als Commands speichert
class CommandRecorder {
    val commands = mutableListOf<Command>()

    fun moveArm(x: Double, y: Double, z: Double) {
        commands.add(Command.MoveArm(x, y, z))
    }

    fun grib(power: Int) {
        commands.add(Command.Grib(power))
    }

    fun movearm(x: Double) {
        commands.add(Command.MoveArmX(x))
    }
}

// 3. Implementiere die Thread-Methode mit DSL-Erweiterung
class UR {
    // Speichert die Commands aller Threads (optional)
    private val allCommands = mutableListOf<Command>()

    fun thread(block: CommandRecorder.() -> Unit) {
        val recorder = CommandRecorder()
        recorder.block() // Fülle die Commands-Liste im Recorder

        // Füge die Commands zur globalen Liste hinzu (optional)
        allCommands.addAll(recorder.commands)

        // Starte den Thread und führe die gespeicherten Commands aus
        thread {
            executeCommands(recorder.commands)
        }
    }

    private fun executeCommands(commands: List<Command>) {
        commands.forEach { command ->
            when (command) {
                is Command.MoveArm -> moveArm(command.x, command.y, command.z)
                is Command.Grib -> grib(command.power)
                is Command.MoveArmX -> movearm(command.x)
            }
        }
    }

    // Echte Implementierung der Methoden (Beispiel)
    private fun moveArm(x: Double, y: Double, z: Double) {
        println("Arm bewegt zu ($x, $y, $z)")
    }

    private fun grib(power: Int) {
        println("Greifen mit Power $power")
    }

    private fun movearm(x: Double) {
        println("Arm bewegt zu X: $x")
    }
}

// 4. Beispiel-Nutzung
fun main22() {
    val ur = UR()
    ur.thread {
        moveArm(2.0, 2.0, 2.0)
        grib(20)
        movearm(2.1)
    }

    // Warte kurz, um den Thread abzuschließen (nur für Demo-Zwecke)
    Thread.sleep(1000)
}