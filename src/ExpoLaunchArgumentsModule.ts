import { requireNativeModule } from 'expo-modules-core';
import { ExpoLaunchArgumentsModule } from './ExpoLaunchArguments.types';

// It loads the native module object from the JSI or falls back to
// the bridge module (from NativeModulesProxy) if the remote debugger is on.
export default requireNativeModule('ExpoLaunchArguments') as ExpoLaunchArgumentsModule;
