package com.wolfscowl.ur_client.test

import com.wolfscowl.ur_client.test.cmdThread.Gripper
import com.wolfscowl.ur_client.test.cmdThread.Robot

// =====================Generische invokeFunktion with extra Interface + Optimierung ==============


//sealed class Tool<T : RobotTool> {
//
//
//    class GripperTool() : Tool<Gripper>() {
//        companion object {
//            // Mit operator invoke wird hier direkt das fertige RobotTool zurückgegeben.
//            operator fun invoke(toolIndex: Int, robot: ToolAction): Gripper =
//                Gripper(robot, "RG2", toolIndex)
//        }
//    }
//
//    class ScrewdriverTool() : Tool<Screwdriver>() {
//        companion object {
//            operator fun invoke(toolIndex: Int, robot: ToolAction): Screwdriver =
//                Screwdriver(robot, "screwdriver", toolIndex)
//        }
//    }
//}
//
//// Gemeinsames Interface für alle Werkzeuge
//sealed interface RobotTool {
//    val robot: ToolAction
//    val model: String
//    val toolIndex: Int
//    fun interpreteTool(data: String): String
//}
//
//
//// Greifer-Werkzeug mit eigenen Methoden
//data class Gripper(override val robot: ToolAction, override val model: String, override val toolIndex: Int) : RobotTool {
//    fun grip() { robot.sendData("rpcxml.grip()") }
//    fun release() { robot.sendData("rpcxml.release()") }
//    override fun interpreteTool(data: String): String {
//        return "something"
//    }
//}
//
//// Schraubendreher-Werkzeug mit eigenen Methoden
//data class Screwdriver(override val robot: ToolAction, override val model: String, override val toolIndex: Int) : RobotTool {
//    fun turnLeft() { robot.sendData("rpcxml.left()") }
//    fun turnRight() { robot.sendData("rpcxml.right()") }
//    override fun interpreteTool(data: String): String {
//        return "something"
//    }
//}
//
//// Gemeinsames Interface für alle Werkzeuge
//sealed interface ToolAction {
//    fun sendData(script: String)
//}
//
//
//// Roboterklasse mit generischer Factory-Methode
//class Robot(val host: String): ToolAction {
//    override fun sendData(script: String) {
//        println("Send script to $host: $script")
//    }
//
//}
//
//class Repository() {
//    // Jetzt kannst du den Roboter ohne explizite Typangabe erstellen:
//    var robot = Robot("192.168.0.1")
//    var tool: RobotTool? = null
//    var toolFactory: (() -> RobotTool)? = null
//
//    private inline fun <reified T : RobotTool> withTool(action: (T) -> Unit) {
//        if (tool is T) {
//            action(tool as T)
//        }
//    }
//
//    fun changeHost(host: String) {
//        robot = Robot(host)
//        tool = toolFactory?.invoke()
//        // Wie weise ich hier das dem Tool den neuen Roboter hinzu
//        // Das Tool soll das gleiche bleiben mit dem verweis auf den neuen roboter
//    }
//
//    fun createTool(toolIndex: Int) {
//        tool = Tool.GripperTool(toolIndex, robot)
//        toolFactory = { Tool.GripperTool(toolIndex, robot) }
//    }
//
//    fun grip() {
//        withTool<Gripper> { it.grip() }
//    }
//}
//
////
////// Hauptprogramm
//fun main() {
//    val robotRepo = Repository()
//
//    println("1 Robot: " + robotRepo.robot )
//    robotRepo.createTool(2)
//    println("1 Robot: " + robotRepo.tool?.robot)
//    println("1 Robot: " + robotRepo.tool?.toolIndex)
//
//    robotRepo.changeHost("192.168.2.2")
//    println("2 Robot: " + robotRepo.robot )
//    println("2 Robot: " + robotRepo.tool?.robot)
//    println("2 Robot: " + robotRepo.tool?.toolIndex)
//
//    robotRepo.grip()
//}



// ===================================== Generischer 2 Tool ========================================

// ====== Ohne Interface ToolAction ==============

//sealed class Tool<T : RobotTool> {
//    abstract fun create(robot: Robot<*, *>): T
//
//    class GripperTool(val toolIndex: Int) : Tool<Gripper>() {
//        override fun create(robot: Robot<*, *>): Gripper =
//            // Der Roboter wird hier komplett übergeben.
//            Gripper(robot, "RG2", toolIndex)
//    }
//
//    class ScrewdriverTool(val toolIndex: Int) : Tool<Screwdriver>() {
//        override fun create(robot: Robot<*, *>): Screwdriver =
//            Screwdriver(robot, "screwdriver", toolIndex)
//    }
//
//    object NoneTool : Tool<NoTool>() {
//        override fun create(robot: Robot<*, *>): NoTool = NoTool
//    }
//}

// ====== Mit Interface ToolAction ==============

//sealed class Tool<T : RobotTool> {
//    abstract fun create(robot: ToolAction): T
//
//    class GripperTool(val toolIndex: Int) : Tool<Gripper>() {
//        override fun create(robot: ToolAction): Gripper = Gripper(robot, "RG2", toolIndex)
//    }
//
//    class ScrewdriverTool(val toolIndex: Int) : Tool<Screwdriver>() {
//        override fun create(robot: ToolAction): Screwdriver = Screwdriver(robot, "screwdriver", toolIndex)
//    }
//
//    object NoneTool : Tool<NoTool>() {
//        override fun create(robot: ToolAction): NoTool = NoTool
//    }
//}
//
//sealed interface RobotTool
//
//object NoTool : RobotTool
//
//// Gemeinsames Interface für alle Werkzeuge
//sealed interface ToolAction {
//    fun sendData(script: String)
//}
//
//class Gripper(val robot: ToolAction, val model: String, val toolIndex: Int) : RobotTool {
//    fun grip() { robot.sendData("rpcxml.grip()") }
//    fun release() { robot.sendData("rpcxml.release()") }
//}
//
//class Screwdriver(val robot: ToolAction, val model: String, val toolIndex: Int) : RobotTool {
//    fun turnLeft() { robot.sendData("rpcxml.left()") }
//    fun turnRight() { robot.sendData("rpcxml.right()") }
//}
//
//class Robot<T1 : RobotTool, T2 : RobotTool>(val host: String) : ToolAction {
//    // Hier werden beide Werkzeuge gespeichert:
//    lateinit var tool1: T1
//    lateinit var tool2: T2
//
//    override fun sendData(script: String) {
//        println("Send script to $host: $script")
//    }
//
//    companion object {
//        // Die Factory-Methode erzeugt einen Roboter mit zwei Tools.
//        operator fun <T1 : RobotTool, T2 : RobotTool> invoke(
//            host: String,
//            tool1: Tool<T1>,
//            tool2: Tool<T2>
//        ): Robot<T1, T2> {
//            val robot = Robot<T1, T2>(host)
//            // Anstelle eines Robot<T> übergeben wir hier 'robot' als ToolAction,
//            // da unsere Tools die Methode create nun so definiert haben.
//            robot.tool1 = tool1.create(robot)
//            robot.tool2 = tool2.create(robot)
//            return robot
//        }
//    }
//
//    // Alternativ: Methoden zum erneuten Erstellen der Werkzeuge, falls nötig.
//    fun createTools(tool1: Tool<T1>, tool2: Tool<T2>) {
//        this.tool1 = tool1.create(this)
//        this.tool2 = tool2.create(this)
//    }
//}
//
//
//
//class Repository() {
//    // Jetzt kannst du den Roboter ohne explizite Typangabe erstellen:
//    var robot: Robot<out RobotTool, out RobotTool> = Robot("192.168.0.1", Tool.NoneTool,Tool.NoneTool)
//    private var tool1 = Tool.GripperTool(1)
//    private var tool2 = Tool.GripperTool(2)
//    private var robotFactory: (Tool<*>,Tool<*>) -> Robot<*,*> = { tool1, tool2 -> Robot("192.168.0.1", tool1, tool2) }
//
//    // Der Typ wird aus dem übergebenen Tool abgeleitet (Robot<Gripper>).
//
//    private inline fun <reified T : RobotTool> Robot<*,*>.withTool(action: (T) -> Unit) {
//        if (tool1 is T) {
//            action(tool1 as T)
//        }
//        if (tool2 is T) {
//            action(tool2 as T)
//        }
//    }
//
//    fun changeHost(host: String) {
//        // Das Tool soll das gleiche bleiben wie zuvor, wie mache ich das
//        robotFactory = { tool1,tool2 -> Robot(host,tool1,tool2) }
//        robot = Robot(host, tool1,tool2)
//    }
//
//    fun createTool1(toolIndex: Int){
//        tool1 = Tool.GripperTool(toolIndex)
//        robot = robotFactory( Tool.GripperTool(toolIndex), tool2 )
//    }
//
//    fun createTool2(toolIndex: Int){
//        tool2 = Tool.GripperTool(toolIndex)
//        robot = robotFactory( tool1 , Tool.GripperTool(toolIndex) )
//    }
//
//    fun grip() {
//        // So kannst du die Methoden deines Tools aufrufen:
//        if (robot.tool1 is Gripper)
//            (robot.tool1 as Gripper).grip()
//
//        robot.withTool<Gripper> { it.grip() }
//    }
//}
//
//
//fun main() {
//    val robotRepo = Repository()
//    val robot = Robot(
//        "192.178.2.2",
//        Tool.ScrewdriverTool(1),
//        Tool.GripperTool(2)
//    )
//
//    robot.tool2.grip()
//
//    println("1 Robot: " + robotRepo.robot )
//    robotRepo.createTool1(2)
//    println("1 Robot: " + (robotRepo.robot.tool1 as Gripper).robot)
//    println("1 Robot: " + robotRepo.robot )
//    println("1 Robot: " + (robotRepo.robot.tool1 as Gripper).toolIndex)
//
//    robotRepo.changeHost("192.168.2.2")
//    println("2 Robot: " + robotRepo.robot )
//    println("2 Robot: " + (robotRepo.robot.tool1 as Gripper).robot)
//    println("2 Robot: " + (robotRepo.robot.tool1 as Gripper).toolIndex)
//
//    robotRepo.grip()
//}

// ===================================== Generischer 1 Tool ========================================


//sealed class Tool<T : RobotTool> {
//    abstract fun create(robot: Robot<T>): T
//
//    class GripperTool(val toolIndex: Int) : Tool<Gripper>() {
//        override fun create(robot: Robot<Gripper>): Gripper = Gripper(robot, "RG2", toolIndex)
//    }
//
//    class ScrewdriverTool(val toolIndex: Int) : Tool<Screwdriver>() {
//        override fun create(robot: Robot<Screwdriver>): Screwdriver = Screwdriver(robot, "screwdriver",toolIndex )
//    }
//
//    object NoneTool : Tool<NoTool>() {
//        override fun create(robot: Robot<NoTool>): NoTool = NoTool
//    }
//}
//
//sealed interface RobotTool
//
//object NoTool : RobotTool
//
//// Gemeinsames Interface für alle Werkzeuge
//sealed interface ToolAction {
//    fun sendData(script: String)
//}
//
//class Gripper(val robot: ToolAction, val model: String, val toolIndex: Int) : RobotTool {
//    fun grip() { robot.sendData("rpcxml.grip()") }
//    fun release() { robot.sendData("rpcxml.release()") }
//}
//
//class Screwdriver(val robot: ToolAction, val model: String, val toolIndex: Int) : RobotTool {
//    fun turnLeft() { robot.sendData("rpcxml.left()") }
//    fun turnRight() { robot.sendData("rpcxml.right()") }
//}
//
//class Robot<T : RobotTool>(val host: String): ToolAction {
//    lateinit var tool: T
//
//    override fun sendData(script: String) {
//        println("Send script to $host: $script")
//    }
//
//    companion object {
//        // Mit dieser Factory-Methode kann der Typ T aus dem Tool abgeleitet werden.
//        operator fun <T : RobotTool> invoke(host: String, tool: Tool<T>): Robot<T> {
//            val robot = Robot<T>(host)
//            robot.tool = tool.create(robot)
//            return robot
//        }
//    }
//
//    // Falls du die Methode auch noch behalten möchtest:
//    fun createTool(tool: Tool<T>): T {
//        this.tool = tool.create(this)
//        return this.tool
//    }
//}
//
//
//
//class Repository() {
//    // Jetzt kannst du den Roboter ohne explizite Typangabe erstellen:
//    var robot: Robot<out RobotTool> = Robot("192.168.0.1", Tool.NoneTool)
//    private var tool = Tool.GripperTool(0)
//    private var robotFactory: (Tool<*>) -> Robot<*> = { Robot("192.168.0.1", it) }
//
//    // Der Typ wird aus dem übergebenen Tool abgeleitet (Robot<Gripper>).
//
//    private inline fun <reified T : RobotTool> Robot<*>.withTool(action: (T) -> Unit) {
//        if (tool is T) {
//            action(tool as T)
//        }
//    }
//
//    fun changeHost(host: String) {
//        // Das Tool soll das gleiche bleiben wie zuvor, wie mache ich das
//        robotFactory = { Robot(host,it) }
//        robot = Robot(host, tool)
//    }
//
//    fun createTool(toolIndex: Int){
//        tool = Tool.GripperTool(toolIndex)
//        robot = robotFactory( Tool.GripperTool(toolIndex) )
//    }
//
//    fun grip() {
//        // So kannst du die Methoden deines Tools aufrufen:
//        if (robot.tool is Gripper)
//            (robot.tool as Gripper).grip()
//
//        robot.withTool<Gripper> { it.grip() }
//    }
//}
//
//
//fun main() {
//    val robotRepo = Repository()
//    val robot = Robot("192.178.2.2",Tool.GripperTool(2))
//    robot.tool.grip()
//
//    println("1 Robot: " + robotRepo.robot )
//    robotRepo.createTool(2)
//    println("1 Robot: " + (robotRepo.robot.tool as Gripper).robot)
//    println("1 Robot: " + robotRepo.robot )
//    println("1 Robot: " + (robotRepo.robot.tool as Gripper).toolIndex)
//
//    robotRepo.changeHost("192.168.2.2")
//    println("2 Robot: " + robotRepo.robot )
//    println("2 Robot: " + (robotRepo.robot.tool as Gripper).robot)
//    println("2 Robot: " + (robotRepo.robot.tool as Gripper).toolIndex)
//
//    robotRepo.grip()
//}



// ===============================Generische CreateFunktion with extra Interface =====================

//sealed class Tool<T : RobotTool> {
//    abstract fun create(robot: Robot): T
//
//    class GripperTool(val toolIndex: Int) : Tool<Gripper>() {
//        override fun create(robot: Robot): Gripper =
//            com.wolfscowl.ur_client.test.cmdThread.Gripper(robot, "RG2", toolIndex)
//    }
//
//    class ScrewdriverTool(val toolIndex: Int) : Tool<Screwdriver>() {
//        override fun create(robot: Robot): Screwdriver = Screwdriver(robot, "screwdriver", toolIndex )
//    }
//}
//
//// Gemeinsames Interface für alle Werkzeuge
//sealed interface RobotTool {
//    val robot: ToolAction
//    val model: String
//    val toolIndex: Int
//    fun interpreteTool(data: String): String
//}
//
//
//// Greifer-Werkzeug mit eigenen Methoden
//data class Gripper(override val robot: ToolAction, override val model: String, override val toolIndex: Int) : RobotTool {
//    fun grip() { robot.sendData("rpcxml.grip()") }
//    fun release() { robot.sendData("rpcxml.release()") }
//    override fun interpreteTool(data: String): String {
//        return "something"
//    }
//}
//
//// Schraubendreher-Werkzeug mit eigenen Methoden
//data class Screwdriver(override val robot: ToolAction, override val model: String, override val toolIndex: Int) : RobotTool {
//    fun turnLeft() { robot.sendData("rpcxml.left()") }
//    fun turnRight() { robot.sendData("rpcxml.right()") }
//    override fun interpreteTool(data: String): String {
//        return "something"
//    }
//}
//
//// Gemeinsames Interface für alle Werkzeuge
//sealed interface ToolAction {
//    fun sendData(script: String)
//}
//
//
//// Roboterklasse mit generischer Factory-Methode
//class Robot(val host: String): ToolAction {
//    override fun sendData(script: String) {
//        println("Send script to $host: $script")
//    }
//
//    // Generische Methode zur Werkzeugerstellung – automatisch richtiger Rückgabetyp!
//    fun <T : RobotTool> createTool(tool: Tool<T>): T {
//        return tool.create(this)
//    }
//}
//
//// Hauptprogramm
//fun main() {
//    val robot = com.wolfscowl.ur_client.test.cmdThread.Robot("192.168.0.1")
//
//    // ✅ KEIN Casting nötig, da der Rückgabewert automatisch erkannt wird!
//    val gripper = robot.createTool(Tool.GripperTool(0))
//    gripper.grip()
//
//    val screwdriver = robot.createTool(Tool.ScrewdriverTool(0))
//    screwdriver.turnLeft()
//}
//
//class Repository() {
//    // Jetzt kannst du den Roboter ohne explizite Typangabe erstellen:
//    var robot = com.wolfscowl.ur_client.test.cmdThread.Robot("192.168.0.1")
//    var tool: RobotTool? = null
//    var toolFactory: (() -> RobotTool)? = null
//    // Der Typ wird aus dem übergebenen Tool abgeleitet (Robot<Gripper>).
//
//    inline fun <reified T : RobotTool> withTool(action: (T) -> Unit) {
//        if (tool is T) {
//            action(tool as T)
//        }
//    }
//
//    fun changeHost(host: String) {
//        robot = com.wolfscowl.ur_client.test.cmdThread.Robot(host)
//        // Wie weise ich hier das dem Tool den neuen Roboter hinzu
//        // Das Tool soll das gleiche bleiben mit dem verweis auf den neuen roboter
//        tool = toolFactory?.invoke()
//    }
//
//    fun createTool() {
//        toolFactory = { robot.createTool(Tool.GripperTool(0)) }
//        tool = toolFactory?.invoke()
//    }
//
//    fun grip() {
//        // So kannst du die Methoden deines Tools aufrufen:
//        if (tool is Gripper)
//            (tool as Gripper).grip()
//
//        withTool<Gripper> { it.grip() }
//
//    }
//}


// ======================================Generische CreateFunktion==================================

//sealed class Tool<T : RobotTool> {
//    abstract fun create(robot: Robot): T
//
//    class GripperTool(val toolIndex: Int) : Tool<Gripper>() {
//        override fun create(robot: Robot): Gripper = Gripper(robot, "RG2", toolIndex)
//    }
//
//    object ScrewdriverTool : Tool<Screwdriver>() {
//        override fun create(robot: Robot): Screwdriver = Screwdriver(robot, "screwdriver")
//    }
//}
//
//// Gemeinsames Interface für alle Werkzeuge
//sealed interface RobotTool
//
//// Greifer-Werkzeug mit eigenen Methoden
//class Gripper(val robot: Robot, val model: String, val toolIndex: Int) : RobotTool {
//    fun grip() { robot.sendData("rpcxml.grip()") }
//    fun release() { robot.sendData("rpcxml.release()") }
//}
//
//// Schraubendreher-Werkzeug mit eigenen Methoden
//class Screwdriver(val robot: Robot, val model: String) : RobotTool {
//    fun turnLeft() { robot.sendData("rpcxml.left()") }
//    fun turnRight() { robot.sendData("rpcxml.right()") }
//}
//
//// Roboterklasse mit generischer Factory-Methode
//class Robot(val host: String) {
//    fun sendData(script: String) {
//        println("Send script to $host: $script")
//    }
//
//    // Generische Methode zur Werkzeugerstellung – automatisch richtiger Rückgabetyp!
//    fun <T : RobotTool> createTool(tool: Tool<T>): T {
//        return tool.create(this)
//    }
//}
//
//// Hauptprogramm
//fun main() {
//    val robot = Robot("192.168.0.1")
//
//    // ✅ KEIN Casting nötig, da der Rückgabewert automatisch erkannt wird!
//    val gripper = robot.createTool(Tool.GripperTool(0))
//    gripper.grip()
//
//    val screwdriver = robot.createTool(Tool.ScrewdriverTool)
//    screwdriver.turnLeft()
//}




// ======================================= Überladene CreateFunktion ===============================

//sealed class Tool {
//    object Gripper : Tool()
//    object Screwdriver : Tool()
//}
//
//// Gemeinsames Interface für alle Werkzeuge
//sealed interface RobotTool
//
//// Greifer-Werkzeug mit eigenen Methoden
//class Gripper(val robot: Robot, val model: String) : RobotTool {
//    fun grip() { robot.sendData("rpcxml.grip()") }
//    fun release() { robot.sendData("rpcxml.release()") }
//}
//
//// Schraubendreher-Werkzeug mit eigenen Methoden
//class Screwdriver(val robot: Robot, val model: String) : RobotTool {
//    fun turnLeft() { robot.sendData("rpcxml.left()") }
//    fun turnRight() { robot.sendData("rpcxml.right()") }
//}
//
//// Roboterklasse mit Factory-Methode
//class Robot(val host: String) {
//    fun sendData(script: String) {
//        println("Send script to $host: $script")
//    }
//
//    // Factory-Methode zur Werkzeugerstellung
//    fun createTool(tool: Tool.Gripper): Gripper {
//        return Gripper(this,"RG2")
//    }
//
//    // Factory-Methode zur Werkzeugerstellung
//    fun createTool(tool: Tool.Screwdriver): Screwdriver {
//        return Screwdriver(this,"screwdriver")
//    }
//}
//
//// Hauptprogramm
//fun main() {
//    val robot = Robot("192.168.0.1")
//
//    // ✅ Kein Casting nötig, direkter Aufruf mit Tool.Gripper
//    val gripper = robot.createTool(Tool.Gripper)
//    gripper.grip()
//
//    val screwdriver = robot.createTool(Tool.Screwdriver) as Screwdriver
//    screwdriver.turnLeft()
//}




// ================================================================================================