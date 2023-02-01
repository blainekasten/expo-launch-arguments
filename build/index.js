// Import the native module. On web, it will be resolved to ExpoLaunchArguments.web.ts
// and on native platforms to ExpoLaunchArguments.ts
import ExpoLaunchArgumentsModule from './ExpoLaunchArgumentsModule';
// Get the native constant value.
export const launchArguments = ExpoLaunchArgumentsModule.launchArguments;
//# sourceMappingURL=index.js.map