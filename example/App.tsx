import { StyleSheet, Text, View } from 'react-native';

import * as ExpoLaunchArguments from 'expo-launch-arguments';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>
        launch args: \n
        {Object.entries(ExpoLaunchArguments.launchArguments)
          .map(([k, v]) => `${k} = ${v}`)
          .join('\n')}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
