import { requireNativeModule } from 'expo-modules-core';
// Safely require the native module.
// This package is not mission critical enough to crash an app for.
let module = { launchArguments: {} };
try {
    module = requireNativeModule('ExpoLaunchArguments');
}
catch { }
export default module;
//# sourceMappingURL=ExpoLaunchArgumentsModule.js.map