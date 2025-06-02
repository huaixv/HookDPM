package com.bintianqi.hookdpm.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringClass
import com.highcapable.yukihookapi.hook.type.java.UnitType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

@InjectYukiHookWithXposed
object HookEntry : IYukiHookXposedInit {
    override fun onHook() = encase {
        loadSystem {
            "com.android.server.devicepolicy.DevicePolicyManagerService".toClass().apply {
                method {
                    name = "enforceCanSetDeviceOwnerLocked"
                    paramCount = 4
                    returnType = UnitType
                }.hook {
                    before {
                        if (prefs.getBoolean("force_do", false)) {
                            resultNull()
                        }
                    }
                }
                method {
                    name = "checkDeviceOwnerProvisioningPreConditionLocked"
                    paramCount = 5
                    returnType = IntType
                }.hook {
                    after {
                        if (prefs.getBoolean("force_do", false)) {
                            result = 0
                        }
                    }
                }
                method {
                    name = "enforceCanSetProfileOwnerLocked"
                    paramCount = 4
                    returnType = UnitType
                }.hook {
                    before {
                        if (prefs.getBoolean("force_po", false)) {
                            resultNull()
                        }
                    }
                }
                method {
                    name = "hasIncompatibleAccountsOnAnyUser"
                    emptyParam()
                    returnType = BooleanType
                }.hook {
                    after {
                        if (prefs.getBoolean("bypass_account_check", false)) {
                            result = false
                        }
                    }
                }
                method {
                    name = "hasAccountsOnAnyUser"
                    emptyParam()
                    returnType = BooleanType
                }.hook {
                    after {
                        if (prefs.getBoolean("bypass_account_check", false)) {
                            result = false
                        }
                    }
                }
                method {
                    name = "nonTestNonPrecreatedUsersExist"
                    emptyParam()
                    returnType = BooleanType
                }.hook {
                    after {
                        if (prefs.getBoolean("enhanced_mode", false)) {
                            result = false
                        }
                    }
                }
                method {
                    name = "isProvisioningAllowed"
                    param(StringClass, StringClass)
                    returnType = BooleanType
                }.hook {
                    after {
                        if (prefs.getBoolean("hook_ipa", false)) {
                            result = true
                        }
                    }
                }
                method {
                    name = "checkProvisioningPrecondition"
                    param(StringClass, StringClass)
                    returnType = IntType
                }.hook {
                    after {
                        if (prefs.getBoolean("hook_cpp", false)) {
                            result = 0
                        }
                    }
                }
            }
        }
    }
}
