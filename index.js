import React from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    Button,
    Alert
} from 'react-native';

import AppNavigate from './AppNavigate'

class HelloWorld extends  React.Component {
    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.hello}>Hello from React Native!</Text>
            </View>
        );
    }
}

class HelloFragment1 extends React.Component {
    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.hello}>Hello Fragment 1!</Text>
                <Text style={styles.subhead}>A second line!</Text>
            </View>
        );
    }
}

class HelloFragment2 extends React.Component {
    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.hello}>Hello Fragment 2!</Text>
                <Button
                    onPress={() => {
                        console.log("requesting nav to /book")
                        AppNavigate.navigate("/home")
                    }}
                    title={"Navigate Home"}
                />
            </View>
        );
    }
}

var styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center'
    },
    buttonContainer: {
       margin: 20
    },
    hello: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10
    },
    subhead: {
        fontSize: 16,
        textAlign: 'center',
        margin: 10
    }
});

AppRegistry.registerComponent(
    'MyReactNativeApp',
    () => HelloWorld
)

AppRegistry.registerComponent(
    'HelloFragment1App',
    () => HelloFragment1
)

AppRegistry.registerComponent(
    'HelloFragment2App',
    () => HelloFragment2
)