import { requireNativeModule } from 'expo-modules-core';
import { ExpoLaunchArgumentsModule } from './ExpoLaunchArguments.types';

// Safely require the native module.
// This package is not mission critical enough to crash an app for.
let module: ExpoLaunchArgumentsModule = { launchArguments: {} };
try {
  module = requireNativeModule(
    'ExpoLaunchArguments',
  ) as ExpoLaunchArgumentsModule;
} catch {}

export default module;
