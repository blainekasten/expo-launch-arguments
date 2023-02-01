import ExpoModulesCore
import Foundation

public class ExpoLaunchArgumentsModule: Module {
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  public func definition() -> ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ExpoLaunchArguments')` in JavaScript.
    Name("ExpoLaunchArguments")

    // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
    Constants([
      "launchArguments": gatherArguments(),
    ])
      
  }
}

private func gatherArguments() -> [String: Any] {
    var dict: [String: Any] = [:]
    let args = ProcessInfo.processInfo.arguments
    
    // args are an array of cli args. Usually something like this:
    // [ "/filepath/to/executable", "--args", ...args ]
    // we need to get the flags that come __after__ "--args"
    let startOfFlagsIndex = (args.firstIndex { $0 == "--args" } ?? -1) + 1
    let flags = Array(args[startOfFlagsIndex...args.count - 1])
    
    for var i in 0 ..< flags.count {
        let flag = flags[i]
        
        if isKey(flag) {
            
            let parts = flag.components(separatedBy: "=")
            
            // --some-flag=foo
            if parts.count == 2 {
                dict[clean(key: parts.first!)] = parts.last ?? ""
            }
            
            // --some-flag or --some-flag Value
            let key = clean(key: flag)
            
            if i + 1 < flags.count {
                let next = flags[i + 1]
                
                if isKey(next) {
                    // --some-flag
                    dict[key] = "true"
                } else {
                    dict[key] = next
                    i += 1
                }
            } else {
                // last argument
                dict[key] = "true";
            }
        }
        // -key value
        else if isKey(flag) && i + 1 < flags.count {
            dict[clean(key: flag)] = flags[i + 1]
            i += 1
        }
    }
    
    return dict
}

private func isKey(_ key: String) -> Bool {
    return key.starts(with: "-")
}

private func clean(key: String) -> String {
    do {
        let regex = try NSRegularExpression(pattern: "-")
        let cleanedKey = regex.stringByReplacingMatches(in: key, options: [], range: NSRange(location: 0, length: key.count), withTemplate: "")
        return cleanedKey
    } catch {
        return key
    }
}
