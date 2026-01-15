package com.wolfscowl.ur_client.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.EOFException
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import javax.swing.text.html.HTML.Tag.S
import kotlin.system.measureTimeMillis


data class StateB(
    @Volatile
    var name: String = "Franz",
    @Volatile
    var age: Int = 20,
)



fun write(): StateB {
    val scope = CoroutineScope(Dispatchers.IO)
    val state = StateB()
    scope.launch{
        println("Start writing")
        for (i in 0..100){
            delay(1)
            state.name = "FranzHansTannz $i"
            println("ersterName geschrieben")
        }
    }
    return state
}

fun main() {
    val myState = write()
    for (i in 0 .. 1000000) {
        println(myState.name)
    }
}


// ==================================================================================================


interface StateAI {
    val name: String
    val number: Int
}

internal data class StateA(private var _name: String = "Hans", private var _number: Int = 0) : StateAI {
    // interne Setter für das Repository oder interne Logik
    internal fun setName(newName: String) { _name = newName }
    internal fun setNumber(newNumber: Int) { _number = newNumber }

    override val name: String get() = _name
    override val number: Int get() = _number
}

class StateRepository {
    private val state = StateA()

    fun getState(): StateAI = state  // Gibt nur das unveränderliche Interface zurück

    fun updateState(newName: String, newNumber: Int) {
        state.setName(newName)  // Nur das Repository kann den Zustand ändern
        state.setNumber(newNumber)
    }
}

fun main2() {
    val repository = StateRepository()
    val state = repository.getState()

    // Der folgende Code funktioniert nicht, weil state nur das Interface ist und unveränderlich:
    // state.name = "Hund"  // ❌ Fehler!

    println(state.name)  // Output: Hans

    // Aber das Repository kann den Zustand intern ändern
    repository.updateState("Hund", 5)

    println(state.name)  // Output: Hund
    println(state.number)  // Output: 5
}


// ==================================================================================================
// ==================================================================================================


//fun main() {
//    val data = byteArrayOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100) // Original-Array
//    println(data.size)
//
//    // Wrappen eines Teilbereichs (z. B. ab Index 3, für 4 Elemente)
//    val buffer = ByteBuffer.wrap(data, 3, 4)
//
////    // Ausgabe der ByteBuffer-Werte
//    println("ByteBuffer: $buffer")
//    println("Position" + buffer.position())
//    println("Limit" + buffer.limit())
//    println("BufferContent: " + buffer.getChar() )
////
////    // Inhalte des Buffers ausgeben
////    while (buffer.hasRemaining()) {
////        print("${buffer.get()} ")
////    }
//}

//// Definiere das Interface für ein Auto
//interface Auto {
//    fun drive()
//    fun honk()
//}
//
//// Konkrete Implementierung: BMW
//class BMW : Auto {
//    override fun drive() {
//        println("BMW fährt los!")
//    }
//
//    override fun honk() {
//        println("BMW hupt: Beep Beep!")
//    }
//}
//
//// Konkrete Implementierung: Audi
//class Audi : Auto {
//    override fun drive() {
//        println("Audi fährt los!")
//    }
//
//    override fun honk() {
//        println("Audi hupt: Hup Hup!")
//    }
//}
//
//// Ein Enum zur Auswahl des Autotyps
//enum class AutoTyp {
//    BMW, AUDI
//}
//
//// Eine Konfigurationsklasse, die den gewünschten Autotyp enthält
//data class AutoConfig(val typ: AutoTyp)
//
//// Delegierende Klasse: Je nach AutoConfig wird die Implementierung von Auto per Delegation zugewiesen
//class AutoDelegator(config: AutoConfig) : Auto by when (config.typ) {
//    AutoTyp.BMW -> BMW()
//    AutoTyp.AUDI -> Audi()
//}
//
//fun main3() {
//    // Beispiel mit BMW:
//    val bmwDelegator = AutoDelegator(AutoConfig(AutoTyp.BMW))
//    bmwDelegator.drive()  // Ausgabe: BMW fährt los!
//    bmwDelegator.honk()   // Ausgabe: BMW hupt: Beep Beep!
//
//    // Beispiel mit Audi:
//    val audiDelegator = AutoDelegator(AutoConfig(AutoTyp.AUDI))
//    audiDelegator.drive() // Ausgabe: Audi fährt los!
//    audiDelegator.honk()  // Ausgabe: Audi hupt: Hup Hup!
//}
//








//=========================================Robot Menu ==============================================



//fun main() {
//    // 139.6.77.184
//    val ur = UR("139.6.77.184",30001)
//    ur.connect()
//    Thread.sleep(1000)
//    if (ur.isConnected) {
//        println("\nConnected to robot!")
//        while (true) {
//            println("\nChoose a command:")
//            println("1. Move robot arm 5 degrees to the left")
//            println("2. Move robot arm 5 degrees to the right")
//            println("3. Close gripper")
//            println("4. Open gripper")
//            println("5. Exit")
//            val input = readLine()
//
//            when (input) {
//                "1" -> moveLeft(ur)
//                "2" -> moveRight(ur)
//                "3" -> closeGripper(ur)
//                "4" -> openGripper(ur)
//                "5" -> break
//                else -> println("Invalid input")
//            }
//        }
//    } else {
//        println("Robot connection failed")
//    }
//}
//
//fun moveLeft(ur: UR) {
//    println("\nCMD: arm to the left")
//    val joints = ur.jointPosition
//    //println("JointPosition:\n" + joints)
//    //println("Press Key to send new JointData: ")
//    joints?.let {
//        ur.movej(
//            q = JointPosition(
//                base = it.base - Math.toRadians(5.0),
//                shoulder = it.shoulder,
//                elbow = it.elbow,
//                wrist1 = it.wrist1,
//                wrist2 = it.wrist2,
//                wrist3 = it.wrist3
//            ),
//            a = 1.2,
//            v = 0.9,
//            t = 0.0,
//            r = 0.0
//        )
//    }
//}
//
//fun moveRight(ur: UR) {
//    println("\nCMD: arm to the right")
//    val joints = ur.jointPosition
//    //println("JointPosition:\n" + joints)
//    //println("Press Key to send new JointData: ")
//    joints?.let {
//        ur.movej(
//            q = JointPosition(
//                base = it.base + Math.toRadians(5.0),
//                shoulder = it.shoulder,
//                elbow = it.elbow,
//                wrist1 = it.wrist1,
//                wrist2 = it.wrist2,
//                wrist3 = it.wrist3
//            ),
//            a = 1.2,
//            v = 0.9,
//            t = 0.0,
//            r = 0.0
//        )
//    }
//}
//
//fun closeGripper(ur: UR) {
//    ur.sendURScript("""
//        global ip_address = "139.6.77.137"
//
//        # min 0 max 37
//        global width_closed = 10.0
//
//        # min 20 N max 85 N
//        global force = 50.0
//
//        # min 10% max 100%
//        global speed = 30
//
//        global tool_index = 0
//
//        gripper = rpc_factory("xmlrpc", "http://" + ip_address + ":41414")
//
//        def grip_ext():
//            gripper.twofg_grip_external(tool_index,width_closed+0.0,force,speed)
//        end
//
//        grip_ext()
//    """.trimIndent())
//}
//
//fun openGripper(ur: UR) {
//    ur.sendURScript("""
//        global ip_address = "139.6.77.137"
//
//        # min 0 max 37
//        global width_closed = 10.0
//        global width_open = 30.0
//
//        # min 20 N max 85 N
//        global force = 50.0
//
//        # min 10% max 100%
//        global speed = 30
//
//        global tool_index = 0
//
//        gripper = rpc_factory("xmlrpc", "http://" + ip_address + ":41414")
//
//        def release_ext():
//            gripper.twofg_grip_external(tool_index,width_open+0.0,force,speed)
//        end
//
//        release_ext()
//    """.trimIndent())
//}













//=========================================API Test==============================================





//fun main() {
//    val ur = UR("139.6.77.184",30001)
//    ur.connect()
//    Thread.sleep(1000)
//    if (ur.isConnected) {
//        println("UR connected!")
//        ur.fetchRobotModel{ println("UR Model: " + it) }
//        Thread.sleep(1000)
//        println("ModeDate:\n" + ur.robotModeData)
//        readLine()
//        val joints = ur.jointPosition
//        println("JointPosition:\n" + joints)
//        println("Press Key to send new JointData: ")
//        readLine()
//        joints?.let {
//            ur.movej(
//                q = JointPosition(
//                    base = it.base + Math.toRadians(5.0),
//                    shoulder = it.shoulder,
//                    elbow = it.elbow,
//                    wrist1 = it.wrist1,
//                    wrist2 = it.wrist2,
//                    wrist3 = it.wrist3
//                ),
//                a = 1.2,
//                v = 0.9,
//                t = 0.0,
//                r = 0.0
//            )
//        }
//        println("Press Key to send Grip Command: ")
//        readLine()
//        // 0 - 37 mm
//        var gripWidth = 10
//        // 20 - 85 N
//        var force = 25
//        // 10 - 100%
//        var speed = 40
//        // 0 - 2
//        // tool_index: Werkzeugposition auf Quick Changer (Standard 0).
//        // Optionen:
//        // 0 - Single,
//        // 1 Dual (Primär),
//        // 2 - Dual (Sekundär)
//        var toolindex = 0
//        ur.sendURScript("""
//            def myMove():
//                twofg_grip_ext($gripWidth, 100, 0)
//            end
//        """.trimIndent())
//        println("Press Key to send Release Command: ")
//        readLine()
//        gripWidth = 25
//        ur.sendURScript("""
//            def myMove():
//                twofg_release_int($gripWidth, $force, $speed, $toolindex)
//
//        """.trimIndent())
//        Thread.sleep(1000)
//
////        readLine()
////        ur.sendURScript( """
////            def myMove():
////                movel(p[0.3,-0.3,0.3, 0,3.14,0], a=1.2, v=0.9)
////            end
////        """.trimIndent())
//
//
//    } else {
//        println("UR Connection failed!")
//    }
//}






//========================================= Socket Test RTDE 02 ====================================

//fun main() {
//    val host = "192.168.178.137" // IP-Adresse des UR-Roboters
//    val port = 30004         // RTDE-Port
//
//    try {
//        // Verbindung herstellen
//        val socket = Socket(host, port)
//        val output: OutputStream = socket.getOutputStream()
//        val input: InputStream = socket.getInputStream()
//
//        // Protokollversion anfordern
//        val protocolVersion = 1
//        val handshakePackage = createProtocolVersionPackage(protocolVersion)
//        output.write(handshakePackage)
//        output.flush()
//
//        // Antwort lesen
//        val response = receiveRTDEMessage(input)
//        println("Protokollversion Antwort: $response")
//
//        // Verbindung schließen
//        socket.close()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}
//
//// Funktion, um ein Protokollversions-Paket zu erstellen
//fun createProtocolVersionPackage(version: Int): ByteArray {
//    val command = ByteBuffer.allocate(5) // 1 Byte für den Pakettyp, 4 Bytes für den Payload
//    command.put(0x56.toByte())           // Pakettyp: 0x56 für REQUEST_PROTOCOL_VERSION
//    command.putInt(version)             // Protokollversion
//    return command.array()
//}
//
//// Funktion, um eine RTDE-Nachricht zu empfangen
//fun receiveRTDEMessage(input: InputStream): String {
//    val header = ByteArray(3) // RTDE Header hat 3 Bytes: 1 Byte Pakettyp, 2 Bytes Länge
//    input.read(header)
//
//    val packageType = header[0].toInt()
//    val payloadLength = (header[1].toInt() shl 8) or header[2].toInt()
//
//    val payload = ByteArray(payloadLength)
//    input.read(payload)
//
//    return "Pakettyp: $packageType, Payload: ${String(payload)}"
//}





//========================================= Socket Test RTDE 01 ====================================


//fun main() {
//    val host = "192.168.178.137" // IP-Adresse des UR-Roboters
//    val port = 30004         // Standardport für RTDE
//
//    try {
//        // Verbindung zum Roboter herstellen
//        val socket = Socket(host, port)
//        val output: OutputStream = socket.getOutputStream()
//        val input: InputStream = socket.getInputStream()
//
//        // Handshake mit dem Roboter (RTDE Protokoll verwenden)
//        sendRTDECommand(output, "REQUEST_PROTOCOL_VERSION")
//        val response = receiveRTDEResponse(input)
//        println("Protokollversion Antwort: $response")
//
//        // Steuerbefehl senden (z. B. Roboter starten)
//        sendRTDECommand(output, "CONTROL_PACKAGE_START")
//        val startResponse = receiveRTDEResponse(input)
//        println("Start Antwort: $startResponse")
//
//        // Datenstream abonnieren
//        sendRTDECommand(output, "SETUP_OUTPUTS")
//        val dataResponse = receiveRTDEResponse(input)
//        println("Datenstream Antwort: $dataResponse")
//
//        // Verbindung schließen
//        socket.close()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}
//
//// Funktion, um RTDE-Kommandos zu senden
//fun sendRTDECommand(output: OutputStream, command: String) {
//    val commandBytes = command.toByteArray()
//    val length = commandBytes.size
//    val buffer = ByteBuffer.allocate(4 + length)
//    buffer.putInt(length)
//    buffer.put(commandBytes)
//    output.write(buffer.array())
//    output.flush()
//}
//
//// Funktion, um RTDE-Antworten zu empfangen
//fun receiveRTDEResponse(input: InputStream): String {
//    val buffer = ByteArray(1024)
//    val bytesRead = input.read(buffer)
//    return String(buffer, 0, bytesRead)
//}











// =================================================================================================
// ========================= Test Primary Interface Breakl Relase ==================================
// =================================================================================================
//fun main() {
//    val robotIp = "192.168.178.137" // Ersetze mit der IP-Adresse deines Roboters
//    val robotPort = 30001 // Port für URScript-Befehle
//
//    val job = CoroutineScope(Dispatchers.IO).launch {
//        val ur = URImpl(Connection.ip,Connection.port)
//        ur.connect()
//        var modeCode: Int = -9999
//        ur.robotModeDataFlow.filterNotNull().collect{ modeData ->
//            if(modeData.robotMode.code != modeCode) {
//                modeCode = modeData.robotMode.code
//                println("RobotMode: " + modeData.robotMode.description)
//            }
//        }
//    }
//
//    try {
//        Thread.sleep(2000)
//        // Verbindung zum Roboter herstellen
//        val socket = Socket(robotIp, robotPort)
//        val outputStream: OutputStream = socket.getOutputStream()
//        val inputStream: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
//        println("Response: " + inputStream.readLine())
//        val commands = listOf<String>(
//            "close popup",
//            "close safety popup",
//            "unlock protective stop",
//            "power on",
//            "brake release"
//        )
//
//        commands.forEach { command ->
//            PrintWriter(outputStream, true).println(command)
//            //println("Response: " + inputStream.readLine())
//            Thread.sleep(1000)
//        }
//    } catch(e: Exception) {
//        println("something went wrong")
//    }
//
//
//
//}


// =================================================================================================
// =================================== Two Sockets ================================================
// =================================================================================================
//fun main() {
//    val robotIp = "192.168.178.137" // Ersetze mit der IP-Adresse deines Roboters
//    val robotPort = 29999 // Port für URScript-Befehle
//    val socket1 = Socket(robotIp,robotPort)
//    val socket2 = Socket(robotIp,robotPort)
//    val inputStream1: BufferedReader = BufferedReader(InputStreamReader(socket1.getInputStream()))
//    val inputStream2: BufferedReader = BufferedReader(InputStreamReader(socket1.getInputStream()))
//    val commands = listOf(
//        "close popup",
//        "close safety popup",
//        "unlock protective stop",
//        "power on",
//        "brake release"
//    )
//    commands.forEach { command ->
//        PrintWriter(socket1.outputStream, true).println(command)
//        println("DashBoard1 Response: " + BufferedReader(InputStreamReader(socket1.inputStream)).readLine())
//        PrintWriter(socket2.outputStream, true).println("PolyscopeVersion")
//        println("DashBoard2 Response: " + BufferedReader(InputStreamReader(socket2.inputStream)).readLine())
//    }
//
//
//
//    println()
//}

// =================================================================================================
// ===================================UR Test================================================
// =================================================================================================

//fun main() {
//    val robotIp = "192.168.178.137" // Ersetze mit der IP-Adresse deines Roboters
//    val robotPort = 29999 // Port für URScript-Befehle
//
//    val job = CoroutineScope(Dispatchers.IO).launch {
//        val ur = URImpl(Connection.ip,Connection.port)
//        ur.connect()
//        var modeCode: Int = -9999
//        ur.robotModeDataFlow.filterNotNull().collect{ modeData ->
//            if(modeData.robotMode.code != modeCode) {
//                modeCode = modeData.robotMode.code
//                println("RobotMode: " + modeData.robotMode.description)
//            }
//        }
//    }
//
//    try {
//        Thread.sleep(2000)
//        // Verbindung zum Roboter herstellen
//        val socket = Socket(robotIp, robotPort)
//        val outputStream: OutputStream = socket.getOutputStream()
//        val inputStream: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
//        println("Response: " + inputStream.readLine())
//        val commands = listOf<String>(
//            "close popup",
//            "close safety popup",
//            "unlock protective stop",
//            "power on",
//            "brake release"
//        )
//
//        commands.forEach { command ->
//            PrintWriter(outputStream, true).println(command)
//            println("Response: " + inputStream.readLine())
//            Thread.sleep(1000)
//        }
//
//
////        val commands2 = """
////            close popup
////            close safety popup
////            unlock protective stop
////            power on
////            brake release
////        """.trimIndent()
////
////        PrintWriter(outputStream, true).println(commands2)
////        println("Response: " + inputStream.readLine())
////        println("Response: " + inputStream.readLine())
////        println("Response: " + inputStream.readLine())
////        println("Response: " + inputStream.readLine())
////        println("Response: " + inputStream.readLine())
//
//
//
//
//
////        PrintWriter(outputStream, true).println(command2)
////        println("Response:" + inputStream.readLine())
//
////        outputStream?.let {
////            PrintWriter(it, true).println(command2)
////            Thread.sleep(500)
////            //readResponse(inputStream)
////            //readResponse(inputStream)
////        }
////
//////        outputStream.write(script.toByteArray(Charsets.UTF_8))
//////        outputStream.flush()
//
//        // Socket schließen
//        Thread.sleep(2000)
//        job.cancel()
//        socket.close()
//    } catch (e: Exception) {
//        e.printStackTrace()
//        println("Fehler bei der Verbindung mit dem Roboter.")
//    }
//}
//
//fun readResponse(inputStream: InputStream): String? {
//    val sb = StringBuffer()
//    var c: Char
//    do {
//        c = inputStream.read().toChar()
//        if (c == '\n') break
//        sb.append(c)
//    } while (c.code != -1)
//    println("Test readResponse: " + sb.toString())
//    return sb.toString()
//}



// ===============================================================================================
// ===============================================================================================
// ===============================================================================================

//fun main () {
//    val ur = URImpl("192.168.178.137",30001 )
//    ur.connect()
//    println("test")
//
//    Thread.sleep(1500)
//
////    println(ur.robotModeData)
////    println(ur.jointData)
////    println(ur.toolData)
////    println(ur.masterBoardData)
////    println(ur.cartesianInfo)
////    println(ur.kinematicsInfo)
//      println(ur.configurationData)
////    println(ur.forceModeData)
////    println(ur.additionalInfo)
////    println(ur.toolCommunicationInfo)
////    println(ur.toolModeInfo)
////    println(ur.versionMessage)
//
//    //Thread.sleep(50000)
//
//    ur.disconnect()
//    println("test2")
//}




// =================================================================================================
// ===================================READ COMMANDS 1================================================
// =================================================================================================





/*
fun main() {
    // IP-Adresse des Roboters und Port
    val robotIp = "192.168.178.137"  // IP-Adresse des UR-Roboters
    val rtdePort = 30004         // RTDE-Port

    try {
        // Verbindung zum RTDE-Port herstellen
        val socket = Socket(robotIp, rtdePort)
        val input = DataInputStream(socket.getInputStream())
        val output = DataOutputStream(socket.getOutputStream())

        // RTDE Protokoll Initialisierung
        println("Verbunden mit RTDE. Initialisiere Kommunikation...")

        // 1. Request RTDE Protokoll-Version
        sendCommand(output, "RTDE_PROTOCOL_VERSION")
        val versionResponse = receiveResponse(input)
        println("RTDE-Version: ${versionResponse.decodeToString()}")

//        // 2. Ausgabe-Register konfigurieren (z. B. Gelenkwinkel, TCP-Position)
//        val outputVariables = "actual_q,actual_TCP_pose"  // Gelenkwinkel, TCP-Positionen
//        sendCommand(output, "RTDE_OUTPUT_SETUP $outputVariables")
//        val setupResponse = receiveResponse(input)
//        println("Setup-Antwort: ${setupResponse.decodeToString()}")
//
//        // 3. Start der Datenübertragung
//        sendCommand(output, "RTDE_START")
//        println("RTDE-Datenübertragung gestartet.")
//
//        // 4. Echtzeitdaten empfangen (Leseschleife)
//        while (true) {
//            val telemetryData = receiveTelemetry(input)
//            println("Empfangene Telemetriedaten: $telemetryData")
//            Thread.sleep(100)  // Abtastzeit (z. B. 100 ms)
//        }

    } catch (e: Exception) {
        println("Fehler bei der RTDE-Kommunikation: ${e.message}")
    }
}

// Hilfsfunktion: Sende einen RTDE-Befehl
fun sendCommand(output: DataOutputStream, command: String) {
    val commandBytes = command.toByteArray()
    val length = commandBytes.size + 3
    val buffer = ByteBuffer.allocate(length)
    buffer.order(ByteOrder.BIG_ENDIAN)
    buffer.put(length.toByte())
    buffer.put(0x01.toByte())  // Paket-Typ: RTDE Command
    buffer.put(commandBytes)
    output.write(buffer.array())
    output.flush()
}

// Hilfsfunktion: Empfange eine Antwort vom RTDE-Port
fun receiveResponse(input: DataInputStream): ByteArray {
    val length = input.readUnsignedByte()
    val packetType = input.readUnsignedByte()
    val payload = ByteArray(length - 3)
    input.readFully(payload)
    return payload
}

// Hilfsfunktion: Empfange Telemetriedaten
fun receiveTelemetry(input: DataInputStream): List<Double> {
    val length = input.readUnsignedByte()
    val packetType = input.readUnsignedByte()
    if (packetType == 0x04) {  // RTDE Data Package
        val payload = ByteArray(length - 3)
        input.readFully(payload)
        val buffer = ByteBuffer.wrap(payload)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        val telemetryValues = mutableListOf<Double>()
        while (buffer.remaining() >= 8) {
            telemetryValues.add(buffer.double)
        }
        return telemetryValues
    }
    return emptyList()
}

*/
// =================================================================================================
// ===================================READ COMMANDS 2================================================
// =================================================================================================

//fun main() {
//    val ip = "192.168.178.137"  // IP-Adresse des UR5-Roboters
//    val port = 30003          // Verwenden Sie den passenden Port
//
//    try {
//
//
//        val socket = Socket(ip, port)
//        println("Verbunden mit UR5 Roboter")
//
//        // Input-Stream für das Empfangen der Daten
//        val inputStream = DataInputStream(socket.getInputStream())
//
//        // Optional: Output-Stream für Befehle
//        val outputStream = DataOutputStream(socket.getOutputStream())
//
//
//        while (true) {
//            // Lies die eingehenden Daten in einem Byte-Array
//            val buffer = ByteArray(2048) // Größe je nach Datenprotokoll
//            val bytesRead = inputStream.read(buffer)
//
//            if (bytesRead > 0) {
//                // Verarbeite die Daten (z. B. Gelenkpositionen extrahieren)
//                val data = buffer.sliceArray(0 until bytesRead)
//                //println("Empfangene Daten: ${data.joinToString(",")}")
//
//                // Beispiel: Dekodiere Gelenkpositionen aus Binärdaten (Dummy-Parser)
//                parseDouble(data)
//            }
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}
//
//
//// Funktion zum Parsen von Gelenkpositionen (Dummy-Parser)
//fun parseDouble(data: ByteArray): List<Double> {
//    val bytesPerDouble = 8
//    val numbersOfDoubles = 151
//
//    for (i in 0 until numbersOfDoubles) {
//        val start = i * bytesPerDouble
//        val end = start + bytesPerDouble
//
//        // Extrahiere 8 Bytes für jedes Gelenk
//        val jointBytes = data.sliceArray(start until end)
//
//        // Konvertiere die 8 Bytes in einen Double-Wert unter Berücksichtigung von Little Endian
//        val jointPosition = ByteBuffer.wrap(jointBytes)
//            .order(ByteOrder.LITTLE_ENDIAN) // Little Endian für UR-Roboter
//            .double
//
//        println ("Double Wert $i: $jointPosition")
//    }
//    println ("=======================================")
//
//
//    // Implementiere hier die Binärdaten-Dekodierung gemäß UR-Protokoll
//    // Zum Beispiel: Floats in den Datenstrom decodieren
//    return listOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0) // Platzhalter
//}
//
//
//


// =================================================================================================
// ===================================READ COMMANDS================================================
// =================================================================================================


//fun main() {
//    val ip = "192.168.178.137"  // IP-Adresse des UR5-Roboters
//    val port = 30002          // Verwenden Sie den passenden Port
//
//
//
//    try {
//        // Socket-Verbindung zum Roboter herstellen
//        val socket = Socket(ip, port)
//        println("Verbunden mit UR5 Roboter")
//
//        for (i in 0 .. 10) {
//            // Stream für Daten senden
//            val buffer = ByteArray(2048)
//            val inputStream: InputStream = socket.getInputStream()
//            read(inputStream, buffer, 0, 5)
//            val bb = ByteBuffer.wrap(buffer, 0, 5)
//            val packageSize = bb.getInt(0)
//            val packageType = bb[4].toInt()
//            val n: Int = packageSize - 5
//            read(inputStream, buffer, 0, n)
//
//            println("Type: " + packageType + "   Size: " + packageSize)
//        }
////        val outputStream: OutputStream = socket.getOutputStream()
////        val writer = PrintWriter(outputStream, true)
////        //movej([1.57, -1.57, 0.0, -1.57, 0.0, 0.0], a=1.4, v=1.05)
////        //movel(p[0.3,-0.3,0.3, 0,3.14,0], a=1.2, v=0.9)
////        // Beispiel: URScript-Befehl senden, um den Roboter zu bewegen
////        val moveCommand = """
////            def myMove():
////                movel(p[0.3,-0.3,0.3, 0,3.14,0], a=1.2, v=0.9)
////            end
////        """.trimIndent()
////
////        /*
////        1 = Y
////        2 =
////         */
////        // Befehl senden
////        writer.println(moveCommand)
////        println("Befehl gesendet: $moveCommand")
////
////        // Verbindung schließen
////        writer.close()
//        socket.close()
//        println("Verbindung geschlossen")
//
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}


object Utils {
    @Throws(IOException::class)
    fun read(`is`: InputStream, data: ByteArray?, off: Int, len: Int) {
        var count: Int
        var n = 0
        while (n < len) {
            count = `is`.read(data, off + n, len - n)
            if (count < 0) throw EOFException()
            n += count
        }
    }

    fun normalizeAngle(angle: Double): Double {
        return Math.atan2(Math.sin(angle), Math.cos(angle))
    }
}



// =================================================================================================
// ===================================WRITE COMMANDS================================================
// =================================================================================================


//fun main2() {
//    val ip = "192.168.178.137"  // IP-Adresse des UR5-Roboters
//    val port = 30002          // Verwenden Sie den passenden Port
//
//    try {
//        // Socket-Verbindung zum Roboter herstellen
//        val socket = Socket(ip, port)
//        println("Verbunden mit UR5 Roboter")
//
//        val outputStream: OutputStream = socket.getOutputStream()
//        val writer = PrintWriter(outputStream, true)
//        //movej([1.57, -1.57, 0.0, -1.57, 0.0, 0.0], a=1.4, v=1.05)
//        //movel(p[0.3,-0.3,0.3, 0,3.14,0], a=1.2, v=0.9)
//        // Beispiel: URScript-Befehl senden, um den Roboter zu bewegen
//        val moveCommand = """
//            def myMove():
//                movel(p[0.3,-0.3,0.3, 0,3.14,0], a=1.2, v=0.9)
//            end
//        """.trimIndent()
//
//        /*
//        1 = Y
//        2 =
//         */
//        // Befehl senden
//        writer.println(moveCommand)
//        println("Befehl gesendet: $moveCommand")
//
//        // Verbindung schließen
//        writer.close()
//        socket.close()
//        println("Verbindung geschlossen")
//
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}



