package com.wolfscowl.ur_client.test

object Script01Backup {
    val script = """
def unnamed2():
  twofg_index = 0
  twofg_dataRead_running = True
  twofg_Width_ext_arr=[0,0,0]
  twofg_Width_int_arr=[0,0,0]
  twofg_Grip_detected_arr=[False,False,False]
  twofg_Busy_arr=[False,False,False]
  ON_TOOL_SHIFT_BOOL_ARR = [74, 78, 78]
  ON_TOOL_SHIFT_INT_ARR = [35, 39, 39]
  ON_TOOL_SHIFT_FLOAT_ARR = [34, 36, 36]
  
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
  
  sleep(2)
  
  thread twofg_dataRead_thread():
    while twofg_dataRead_running:
        sync()
        twofg_dataRead_RTDE(twofg_index)
    end
  end
  
  thrd = run twofg_dataRead_thread()
  
  # ====== Start Movement =====
 
  on_tool_xmlrpc = rpc_factory("xmlrpc", "http://139.6.77.137:41414")
  retVal=on_tool_xmlrpc.twofg_grip_external(0,0.0,40,10)
  # textmsg("Gripper bussy ", twofg_Busy_arr[twofg_index])
  # ======= Blocking =========
  
  timeout=0
  while not twofg_Busy_arr[twofg_index]:
    sleep(0.008)
    timeout=timeout+1
    if timeout>20:
        break
    end 
  end
  
  # textmsg("Gripper bussy ", twofg_Busy_arr[twofg_index])
  
  while(twofg_Busy_arr[twofg_index]==True):
    sync()
  end 
 
  
end
""".trimIndent()

}