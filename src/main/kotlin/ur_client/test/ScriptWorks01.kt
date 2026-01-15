package com.wolfscowl.ur_client.test

object ScriptWorks01 {
    val script = """
        def unnamed2():
            twofg_index = 0
            twofg_dataRead_running = True
            twofg_Width_ext_arr=[0,0,0]
            twofg_Width_int_arr=[0,0,0]
            twofg_Grip_detected_arr=[False,False,False]
            twofg_Busy_arr=[False,False,False]
            ON_CONN_SHIFT_BOOL = 74
            ON_CONN_SHIFT_INT = 34
            ON_CONN_SHIFT_FLOAT = 34
            ON_CONN_REG_SUM_BOOL = 4
            ON_CONN_REG_SUM_INT = 5
            ON_CONN_REG_SUM_FLOAT = 2
            ON_TOOL_SHIFT_BOOL = 74
            ON_TOOL_SHIFT_INT = 35
            ON_TOOL_SHIFT_FLOAT = 34
            ON_TOOL_SHIFT_BOOL_ARR = [74, 78, 78]
            ON_TOOL_SHIFT_INT_ARR = [35, 39, 39]
            ON_TOOL_SHIFT_FLOAT_ARR = [34, 36, 36]
            ON_TOOL_REG_SUM_BOOL = 4
            ON_TOOL_REG_SUM_INT = 4
            ON_TOOL_REG_SUM_FLOAT = 2
            ON_REGISTERS_SPEEDL_FLOAT = 0
            ON_REG_USE_TOOL = False
            
            on_conn_ip = "192.168.12.146"
            on_tool_ip = "192.168.12.146"
            on_device_socket_port = 51234
            ON_DEBUG_LOG = False
            on_conn_rtde_feed_name="rtdeFeedConn"
            on_tool_rtde_feed_name="rtdeFeedTool"
            on_devices = 2
            ON_INIT_WATCHDOG_HZ=5
            
            on_portopened_javaSocket=False
            on_rtde_feed_opened=False
            on_dataProcess_running=False
            
            on_rtde_feed_error_textmsg_title="OnRobot - RTDE error:"
            on_rtde_feed_error="RTDE feed error. OnRobot device count mismatch.<br>Program halted."
            on_rtde_feed_tool_error="Tool RTDE feed error. OnRobot device count mismatch.<br>Program halted."
            on_rtde_feed_open_error_textmsg="Socket 'rtdeFeed' opening was unsuccessful."
            on_rtde_feed_error_title="OnRobot - RTDE error"
            on_rtde_feed_count_error="Invalid RTDE offset setup detected. Please check RTDE Offsets at the OnRobot Setup page in the Installation Tab.<br>Program halted."
            on_rtde_feed_open_error="Establishing connection with the devices was timed out.<br>Ensure that the OnRobot devices are operational, and check the status in the OnRobot Setup page in the Installation Tab."
            
          
            # ===================== RTDE FEED - START ========================== #
            
            
            def on_rtde_feed_close(rtdeFeedName):
              socket_close(rtdeFeedName)
              on_rtde_feed_opened=False
            end 
            
            def on_rtde_feed_open(deviceIP,rtdeFeedName,regStart,regSum,regSpeedl):
                on_rtde_feed_close(rtdeFeedName)
                if((regStart[0]+regSum[0])>128)or((regStart[1]+regSum[1])>48)or((regStart[2]+regSum[2])>48):
                    popup(on_rtde_feed_count_error,title=on_rtde_feed_error_title,error=True,blocking=False)
                    textmsg(str_cat("RegStart: ",regStart),str_cat("	-	RegSum: ",regSum))
                    halt
                end 
                on_rtde_feed_opened=socket_open(deviceIP,on_device_socket_port,rtdeFeedName)
                if not on_rtde_feed_opened:
                    on_rtde_feed_opened=socket_open(deviceIP,on_device_socket_port,rtdeFeedName)
                end 
                if not on_rtde_feed_opened:
                    textmsg(on_rtde_feed_error_textmsg_title,on_rtde_feed_open_error_textmsg)
                    popup(on_rtde_feed_open_error,title=on_rtde_feed_error_title,error=True,blocking=False)
                    halt
                end 
                socket_send_int(regStart[0],rtdeFeedName)
                socket_send_int(regSum[0],rtdeFeedName)
                socket_send_int(regStart[1],rtdeFeedName)
                socket_send_int(regSum[1],rtdeFeedName)
                socket_send_int(regStart[2],rtdeFeedName)
                socket_send_int(regSum[2],rtdeFeedName)
                socket_send_int(regSpeedl,rtdeFeedName)
                socket_send_int(on_devices,rtdeFeedName)
            end 
            
            def on_set_rtde_watchdog(updateHz=ON_INIT_WATCHDOG_HZ):
                local effect="stop"
                if(updateHz<1):
                    effect="ignore"
                end 
                watchdog_conn_reg_str=str_cat("input_int_register_",ON_CONN_SHIFT_INT)
                rtde_set_watchdog(watchdog_conn_reg_str,updateHz,effect)
                if(ON_REG_USE_TOOL):
                    watchdog_tool_reg_str=str_cat("input_int_register_",ON_TOOL_SHIFT_INT_ARR[0])
                    rtde_set_watchdog(watchdog_tool_reg_str,updateHz,effect)
                end 
                if ON_DEBUG_LOG:
                    local update_str=str_cat(" "+effect+" watchdog set to [Hz]: ",updateHz)
                    textmsg(watchdog_conn_reg_str,update_str)
                    if(ON_REG_USE_TOOL):
                        local update_str=str_cat(" "+effect+" watchdog set to [Hz]: ",updateHz)
                        textmsg(watchdog_tool_reg_str,update_str)
                    end 
                end 
            end 

      
            # ===================== RTDE FEED - END ============================ #
            # ===================== DATA READ - START ========================== #
          
          
            def twofg_dataRead_RTDE(tool_index):
            textmsg("Read RTDE ")
            enter_critical
                local reg_offset_bool=ON_TOOL_SHIFT_BOOL_ARR[tool_index]
                local reg_offset_int=ON_TOOL_SHIFT_INT_ARR[tool_index]
                local reg_offset_float=ON_TOOL_SHIFT_FLOAT_ARR[tool_index]
                floatRegDummy=read_input_float_register(reg_offset_float+0)
                twofg_Width_ext_arr[tool_index]=floatRegDummy
                floatRegDummy=read_input_float_register(reg_offset_float+1)
                twofg_Width_int_arr[tool_index]=floatRegDummy
                boolRegDummy=read_input_boolean_register(reg_offset_bool+0)
                twofg_Busy_arr[tool_index]=boolRegDummy
                textmsg("Gripper bussy ", boolRegDummy)
                boolRegDummy=read_input_boolean_register(reg_offset_bool+1)
                twofg_Grip_detected_arr[tool_index]=boolRegDummy
                textmsg("Gripper Detection ", boolRegDummy)
            exit_critical
            end
          
          
          
            thread twofg_dataRead_thread():
            while twofg_dataRead_running:
                sync()
                twofg_dataRead_RTDE(twofg_index)
            end
            end
        
        
            #  ==================== DATA READ - END ============================ #
            #  ==================== INIT - START =============================== #
            
            
            sync()
            # textmsg(on_xmlrpc_start_ip,on_conn_ip)
            if(ON_REG_USE_TOOL):
                on_regStart_conn=[ON_CONN_SHIFT_BOOL,ON_CONN_SHIFT_INT,ON_CONN_SHIFT_FLOAT]
                on_regSum_conn=[ON_CONN_REG_SUM_BOOL,ON_CONN_REG_SUM_INT,ON_CONN_REG_SUM_FLOAT]
                on_rtde_feed_open(on_conn_ip,on_conn_rtde_feed_name,on_regStart_conn,on_regSum_conn,ON_REGISTERS_SPEEDL_FLOAT)
                sync()
                # textmsg(on_xmlrpc_start_ip,on_tool_ip)
                on_regStart_tool=[ON_TOOL_SHIFT_BOOL,ON_TOOL_SHIFT_INT,ON_TOOL_SHIFT_FLOAT]
                on_regSum_tool=[ON_TOOL_REG_SUM_BOOL,ON_TOOL_REG_SUM_INT,ON_TOOL_REG_SUM_FLOAT]
                on_rtde_feed_open(on_tool_ip,on_tool_rtde_feed_name,on_regStart_tool,on_regSum_tool,0)
                sync()
            else:
                on_regStart_conn=[ON_CONN_SHIFT_BOOL,ON_CONN_SHIFT_INT,ON_CONN_SHIFT_FLOAT]
                on_regSum_conn=[ON_CONN_REG_SUM_BOOL,ON_CONN_REG_SUM_INT,ON_CONN_REG_SUM_FLOAT]
                on_rtde_feed_open(on_conn_ip,on_conn_rtde_feed_name,on_regStart_conn,on_regSum_conn,ON_REGISTERS_SPEEDL_FLOAT)
                sync()
            end
            on_set_rtde_watchdog(updateHz=0.2)
            sync()
           
            thrd = run twofg_dataRead_thread()
            
            
            # ===================== INIT - END ================================= #
            # ===================== RUN PROGRAM - START ======================== #

            
            on_tool_xmlrpc = rpc_factory("xmlrpc", "http://192.168.12.146:41414")
            retVal=on_tool_xmlrpc.twofg_grip_external(0,0.0,40,10)
            # textmsg("Gripper bussy ", twofg_Busy_arr[twofg_index])
            
            # ======= Blocking =========
            
            timeout=0
            while not twofg_Busy_arr[twofg_index]:
                sleep(0.008)
                timeout=timeout+1
                if timeout>30:
                    break
                end 
            end
            
            # textmsg("Gripper bussy ", twofg_Busy_arr[twofg_index])
            
            while(twofg_Busy_arr[twofg_index]==True):
             sync()
            end 
            
           
            # ===================== RUN PROGRAM - END ========================== #
          
        end
        """.trimIndent()

}