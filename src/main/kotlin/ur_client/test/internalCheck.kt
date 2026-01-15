package com.wolfscowl.ur_client.test

// =================================================================================================
// ============================= Lösung 1 ==========================================================
// URToolControler nicht Teil der abstrakten Klassen und private in der öffentlichen klasse
// =================================================================================================



//// Diese Klasse soll von außen sichtbar sein
//class Roboty : URToolControllery {
//    override fun sendScript(script: String) {
//        println("send")
//    }
//}
//
//// Interface nur innerhalb des Moduls
//internal interface URToolControllery {
//    fun sendScript(script: String)
//}
//
//// Basisklasse ohne ur-Property
//abstract class Tooly
//
//// Öffentliche abstrakte Klasse
//abstract class OnRobotTooly : Tooly() {
//    abstract val host: String
//    abstract val toolIndex: Int
//}
//
//// RGTool mit privater ur-Property
//data class RGTool internal constructor(
//    override val host: String,
//    override val toolIndex: Int,
//    private val ur: URToolControllery  // Direkt privat
//) : OnRobotTooly() {
//    fun grip() {
//        ur.sendScript("I grip now")
//    }
//}
//
//// Main-Funktion außerhalb des Moduls
//fun main() {
//    val robot = Roboty()
//    val rgTool = RGTool("2334", 2, robot)  // Fehler: internal Konstruktor
//    // rgTool.ur  // Kompilierfehler: Property ist privat
//}






// =================================================================================================
// ============================= Lösung 1 ==========================================================
// URToolControler Teil der abstrakten Klassen und aber internal
// Sichtbarkeit nach ausßen innerhalb des Moduls der Öffentlich Klasse
// =================================================================================================

//// Diese Klasse soll von außen sichtbar sein
//class Roboty : URToolControllery {
//    override fun sendScript(script: String) {
//        println("send")
//    }
//}
//
//// Interface nur innerhalb des Moduls sichtbar
//internal interface URToolControllery {
//    fun sendScript(cript: String)
//}
//
//// Basisklasse mit internal statt protected
//abstract class Tooly {
//    internal abstract val ur: URToolControllery  // Geändert zu internal
//}
//
//// Öffentliche abstrakte Klasse
//abstract class OnRobotTooly : Tooly() {
//    abstract val host: String
//    abstract val toolIndex: Int
//}
//
//// Data-Klasse mit privater ur-Property
//data class RGTool internal constructor(
//    override val host: String,
//    override val toolIndex: Int,
//    private val _ur: URToolControllery  // Private backing field
//) : OnRobotTooly() {
//    override val ur: URToolControllery  // Überschreiben als internal
//        get() = _ur
//
//    fun grip() {
//        _ur.sendScript("I grip now")
//    }
//}
//
//// Main-Funktion außerhalb des Moduls
//fun main() {
//    val robot = Roboty()
//    val rgTool = RGTool("2334", 2, robot)  // Fehler: internal Konstruktor nicht sichtbar
//    val test = rgTool.ur  // Würde selbst bei Sichtbarkeit einen Fehler geben, da internal in Tooly
//}




// =================================================================================================
// ============================= Lösung 3 ==========================================================
//  Main Problem
//
// =================================================================================================

//
//class Roboty() : URToolControllery {
//    override fun sendScript(script: String) {
//        println("send")
//    }
//}
//
//// Dieses Interface soll NICHT von außen sichtbar sein
//internal interface URToolControllery {
//    fun sendScript(script: String)
//}
//
//// Diese Klasse soll von außen des Moduls sichtbar sein
//abstract class Tooly {
//    abstract val model: String
//    protected abstract val ur: URToolControllery
//}
//
//// Diese Klasse soll von außen des Moduls sichtbar sein
//abstract class OnRobotTooly : Tooly() {
//    abstract val host: String
//    abstract val toolIndex: Int
//}
//
//// Diese Klasse soll von außen des Moduls sichtbar sein
//data class RGTool internal constructor(
//    override val host: String,
//    override val toolIndex: Int,
//    override val ur: URToolControllery,
//) : OnRobotTooly() {
//
//    override val model: String = "RGTool R6"
//
//    fun grip() {
//        ur.sendScript("I grip now")
//    }
//}
//
//
//// Innerhalb des Moduls main function für Testzwecke
//fun main() {
//    val robot = Roboty()
//    val rgTool = RGTool("2334",2, robot)
//    rgTool.ur
//}
//
//










// =================================================================================================
// ============================= Lösung 4 ==========================================================
//  Factory Methode
//
// =================================================================================================


// Interner Controller, der nur innerhalb des Moduls sichtbar ist.
internal interface URToolControllery {
    fun sendScript(script: String)
}

// Basis-Klasse für alle Tools.
abstract class Toolyyyy {
    abstract val model: String
    internal abstract val ur: URToolControllery
}

// Spezialisierung für Tools, die an einen Roboter angehängt werden.
abstract class OnRobotTooly : Toolyyyy() {
    abstract val host: String
    abstract val toolIndex: Int
}

// Konkrete Tool-Implementierungen (nur intern konstruierbar).
data class RGTool internal constructor(
    override val host: String,
    override val toolIndex: Int,
    override val ur: URToolControllery,
) : OnRobotTooly() {
    override val model: String = "RGTool R6"

    fun grip() {
        ur.sendScript("I grip now")
    }
}

data class VGTool internal constructor(
    override val host: String,
    override val toolIndex: Int,
    override val ur: URToolControllery,
) : OnRobotTooly() {
    override val model: String = "VGTool V1"

    fun grip() {
        ur.sendScript("I grip now")
    }
}

// Öffentliches Interface, das als Abstraktion für einen Roboter-Controller dient.
// Dadurch wird der interne Typ Roboty verborgen.
public interface URInterfacey {
    fun <T : Toolyyyy> attachTool(factory: ToolFactoryy<T>): T
}

// Öffentliches Interface zur Erstellung von Tools.
// Da hier der Parameter vom Typ RobotController verwendet wird, tauchen interne Typen nicht in der Signatur auf.
public interface ToolFactoryy<T : Toolyyyy> {
    fun create(robot: URInterfacey): T
}



// Internal implementierter Roboter, der als URToolControllery fungiert und gleichzeitig das öffentliche RobotController-Interface erfüllt.
internal class Roboty : URToolControllery, URInterfacey {
    override fun sendScript(script: String) {
        println("sendScript: $script")
    }

    // Hier wird der Tool-Erzeugungsprozess über die Factory eingeleitet.
    override fun <T : Toolyyyy> attachTool(factory: ToolFactoryy<T>): T = factory.create(this)
}



// Öffentliche Factory-Klasse, die Funktionen zur Tool-Erstellung bereitstellt.
// Die interne Implementierung greift über einen Cast (robot as Roboty) auf die interne API zurück.
class URTools {
    companion object {
        fun RGTool(host: String, toolIndex: Int): ToolFactoryy<RGTool> =
            object : ToolFactoryy<RGTool> {
                override fun create(robot: URInterfacey): RGTool {
                    return RGTool(host = host, toolIndex = toolIndex, ur = robot as Roboty)
                }
            }

        fun VGTool(host: String, toolIndex: Int): ToolFactoryy<VGTool> =
            object : ToolFactoryy<VGTool> {
                override fun create(robot: URInterfacey): VGTool {
                    return VGTool(host = host, toolIndex = toolIndex, ur = robot as Roboty)
                }
            }
    }
}

// Um einen Roboter als öffentliche API bereitzustellen, definieren wir hier eine öffentliche Fabrik.
public object RobotFactory {
    fun create(): URInterfacey = Roboty()
}

// Beispiel zur Verwendung
fun main() {
    // Erzeugen eines RobotControllers über die öffentliche RobotFactory.
    val robot = RobotFactory.create()

    // Erzeugen eines RGTool über die öffentliche Factory und Anfügen an den Roboter.
    val rgTool = robot.attachTool(URTools.RGTool(host = "192.168.1.10", toolIndex = 1))
    println("Erzeugtes Tool: ${rgTool.model} an Host ${rgTool.host}")
    rgTool.grip()

    // Ebenso Erzeugen eines VGTool.
    val vgTool = robot.attachTool(URTools.VGTool(host = "192.168.1.20", toolIndex = 2))
    println("Erzeugtes Tool: ${vgTool.model} an Host ${vgTool.host}")
    vgTool.grip()
}

//internal interface URToolControllery {
//    fun sendScript(script: String)
//}
//
//
//abstract class Tooly {
//    abstract val model: String
//    internal abstract val ur: URToolControllery
//}
//
//
//abstract class OnRobotTooly : Tooly() {
//    abstract val host: String
//    abstract val toolIndex: Int
//}
//
//
//data class RGTool internal constructor(
//    override val host: String,
//    override val toolIndex: Int,
//    override val ur: URToolControllery,
//) : OnRobotTooly() {
//
//    override val model: String = "RGTool R6"
//
//    fun grip() {
//        ur.sendScript("I grip now")
//    }
//}
//
//data class VGTool internal constructor(
//    override val host: String,
//    override val toolIndex: Int,
//    override val ur: URToolControllery,
//) : OnRobotTooly() {
//
//    override val model: String = "RGTool R6"
//
//    fun grip() {
//        ur.sendScript("I grip now")
//    }
//}
//
//internal class Roboty() : URToolControllery {
//    override fun sendScript(script: String) {
//        println("send")
//    }
//    fun <T: Tooly>attachTool(urTool: (URToolControllery) -> T): T {
//        return urTool(this as URToolControllery)
//    }
//}
//
//// FactoryKlssse soll von außen sichtbar sein
//class URTools internal constructor() {
//    companion object {
//        fun RGTool(host: String, toolIndex: Int): (URToolControllery) -> RGTool =
//            { controller -> RGTool(host = host,  toolIndex = toolIndex, ur = controller) }
//        fun VGTool(host: String, toolIndex: Int): (URToolControllery) -> RGTool =
//            { controller -> RGTool(host = host,  toolIndex = toolIndex, ur = controller) }
//    }
//}
//
//
//
//// Innerhalb des Moduls main function für Testzwecke
//fun main() {
//    val robot = Roboty()
//    robot.attachTool( URTools.RGTool("192.168.2.1",2) )
//    val rgTool = RGTool("2334",2, robot)
//    rgTool.ur
//}

























