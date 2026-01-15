package com.wolfscowl.ur_client.core

interface URDashBoard {

    /**
     * Loads an specified installation file in Polyscope via the dashboard server.
     * Does not return a response until the load has completed (or failed).
     * The load command fails if the associated installation requires confirmation of safety.
     *
     * **IMPORTANT:**
     * After the installation has been loaded successfully, the robot transitions to the
     * operational state POWER OFF.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     * - `"Loading installation: <default.installation>"`
     * - `"File not found: <default.installation>"`
     * - `"Failed to load installation: <default.installation>"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpInstallation
     */
    fun loadInstallation(installation: String, onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})


    /**
     * Loads a program (*.urp) in Polyscope via the dashboard server (see the program tab in Polyscope).
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     * - `"Loading program: <program.urp>"`
     * - `"File not found: <program.urp>"`
     * - `"Error while loading program: <program.urp>"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpLoad
     */
    fun load(program: String, onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Runs the current program (*.urp) in Polyscope via the dashboard server (see the program tab in Polyscope).
     *
     * **IMPORTANT:**
     * Does not work with stopped or paused URScripts.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     * - `"Starting program"`
     * - `"Failed to execute: play"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpPlay
     */
    fun play(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Stops the current running program via the dashboard server (URscript or Polyscope *.urp file).
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     * - `"Stopped"`
     * - `"Failed to execute: stop"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpStop
     */
    fun stop(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Pauses the current running program via the dashboard server (URscript or local Polyscope *.urp file).
     *
     * **IMPORTANT:**
     * Not recommend for URscripts.
     * If a URscript is in pause mode, it should be stopped with [stop] before executing the next URscript.
     * If this is not done, the next URScript will be terminated immediately.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     * - `"Pausing program"`
     * - `"Failed to execute: pause"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpPause
     */
    fun pause(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})


    /**
     * Queries the execution state of the loaded program (*.urp) or URScript.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     * - `"Program running: true"`
     * - `"Program running: false"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpIsRunning
     */
    fun fetchIsRunning(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit)


    /**
     * Queries the state of the loaded program and file name (*.urp) in polyscope,
     * or STOPPED if no program is loaded.
     *
     * **IMPORTANT:**
     * Does not consider running URScripts!
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     * - `"STOPPED"`
     * - `"PLAYING"`
     * - `"PAUSED"
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpProgramState
     */
    fun fetchProgramState(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit)


    /**
     * Queries the path to loaded program file.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     * - `"Loaded program: <path to loaded program file>"`
     * - `"No program loaded"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.urpLoaded
     */
    fun fetchLoadedProgram(onFailure: (Exception) -> Unit, onResponse: (String) -> Unit)





    /**
     * Powers on the robot arm and releases the brakes to bring it into "normal mode" via the dashboard server.
     *
     * This function sends a series of commands to the UR robot via the dashboard server to fully
     * power it on and make it ready for operation. The following steps are executed in order:
     *
     * 1. Close any open popup dialogs.
     * 2. Close any active safety popups.
     * 3. Unlock a protective stop if one is active.
     * 4. Power on the robot.
     * 5. Release the brakes.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages include:
     * - `"Powering on"`
     * - `"closing safety popup"`
     * - `"Protective stop releasing"`
     * - `"Powering on"`
     * - `"Brake releasing"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.powering
     */
    fun powerOn(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})


    /**
     * Powers off the robot arm via the dashboard server.
     *
     * This function sends a power off command to the connected UR cobot through the Dashboard Server.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages include:
     * - `"Powering off"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.Examples.powering
     */
    fun powerOff(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Queries the model of the robot via the dashboard server.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages include:
     * - `"UR3"`
     * - `"UR5"`
     * - `"UR10"`
     * - ...
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.robotModel
     */
    fun fetchRobotModel(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Closes the current popup (Polyscope) and unlocks protective stop via the dashboard server.
     * The current program will be stopped.
     * The unlock protective stop command fails if less than 5 seconds has passed since
     * the protective stop occurred.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages include:
     * - `"Protective stop releasing"`
     * - `"Cannot unlock protective stop ..."`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.unlockProtectiveStop
     */
    fun unlockProtectiveStop(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Queries the safety status via the dashboard server.
     * A Safeguard Stop resulting from any type of safeguard I/O or a configurable
     * I/O three-position enabling device results in SAFEGUARD_STOP.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages include:
     *
     * `"Safetystatus: <status>"`, where status is
     *  - `"NORMAL"`
     *  - `"REDUCED"`
     *  - `"PROTECTIVE_STOP"`
     *  - `"RECOVERY"`
     *  - `"SAFEGUARD_STOP"`
     *  - `"SYSTEM_EMERGENCY_STOP"`
     *  - `"ROBOT_EMERGENCY_STOP"`
     *  - `"VIOLATION"`
     *  - `"FAULT"`
     *  - `"AUTOMATIC_MODE_SAFEGUARD_STOP"`
     *  - `"SYSTEM_THREE_POSITION_ENABLING_STOP"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.safetyStatus
     */
    fun fetchSafetyStatus(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Queries the serial number of the robot via the dashboard server.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages include:
     * - e.g.`"20196589999"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.serialNumber
     */
    fun fetchSerialNumber(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Queries the Polyscope version via the dashboard server.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages include:
     *  - e.g.`"URSoftware 5.12.6.1102099 (Sep 21 2023)"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.polyscopeVersion
     */
    fun fetchPolyscopeVersion(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Queries the robot mode via the dashboard server
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages may include:
     *
     * `"Robotmode: <mode>"`, where mode is
     * - `"NO_CONTROLLER"`
     * - `"DISCONNECTED,"`
     * - `"CONFIRM_SAFETY"`
     * - `"BOOTING"`
     * - `"POWER_OFF"`
     * - `"POWER_ON"`
     * - `"IDLE"`
     * - `"BACKDRIVE"`
     * - `"RUNNING"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.robotMode
     */
    fun fetchRobotMode(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})

    /**
     * Shuts down and turns off robot and controller via the dashboard server.
     *
     * @param onResponse Optional callback invoked when a response is received from dashboard server.
     * The callback receives the result as a [String]. Response messages include:
     * - `"Shutting down"`
     * @param onFailure Optional callback invoked if the request failed due to a technical error
     * (e.g. connection issues, timeouts). Receives the [Exception] describing the failure.
     *
     * @sample com.wolfscowl.ur_client.examples.ExamplesDoc.shutdown
     */
    fun shutdown(onFailure: (Exception) -> Unit = {}, onResponse: (String) -> Unit = {})
}