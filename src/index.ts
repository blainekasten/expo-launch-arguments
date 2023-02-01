// Import the native module. On web, it will be resolved to ExpoLaunchArguments.web.ts
// and on native platforms to ExpoLaunchArguments.ts
import ExpoLaunchArgumentsModule from './ExpoLaunchArgumentsModule';

console.log({ all: ExpoLaunchArgumentsModule.all });

// Get the native constant value.
export const value = ExpoLaunchArgumentsModule.value;
