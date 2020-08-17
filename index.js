import React from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View,
    Button,
    Alert,
    AsyncStorage,
    NativeEventEmitter
} from 'react-native';
import moment from 'moment';

import AppNavigate from './AppNavigate'

var staticDataItem = '-- empty --';

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

class NotificationsFragment extends React.Component {
    constructor(props) {
        super(props);
        this.state = { persisted: "--empty--"};
        this.retrieve()
    }

    async retrieve() {
        try {
            const value = await AsyncStorage.getItem('key1');
            if ( value !== null ) {
                this.setState( { persisted: value })
            }
        } catch (error ) {
            console.log("Error retrieving Key1")
        }
    }

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
                <Text style={styles.hello}>{this.state.persisted}</Text>

                <Button
                    onPress={() => {
                        console.log("Update clicked")
                        // this.setState( { persisted: "--Updated--"} )
                        _storeData( "Updated " + moment().format("hh:mm:ss")).then(
                           this.retrieve()
                        )
                    }}
                    title={"Update"}
                />
            </View>
        );
    }
}

AppRegistry.registerComponent(
    'NotificationsFragmentRnApp',
    () => NotificationsFragment
)

let _storeData = async ( value ) => {
    try {
        await AsyncStorage.setItem("key1", value );
    } catch (error) {
        console.log( "error storing data");
    }
}

let _retrieveData = async () => {
    try {
        const value = await AsyncStorage.getItem('key1');
    } catch (error ) {

    }
}

class DashboardComponent extends React.Component {
    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.hello}>Dashboard Fragment</Text>
                <Text style={styles.hello}>{staticDataItem}</Text>
                <Button
                    onPress={() => {
                        console.log("requesting nav to /book")
                        AppNavigate.navigate("/home")
                    }}
                    title={"Navigate Home"}
                />
                <Button
                    onPress={() => {
                        console.log("changing static item")
                        staticDataItem = '-- not empty --'
                    }}
                    title={"Change static"}
                />
            </View>
        );
    }

    componentDidMount() {
        console.log("Dashboard Component Mounted")
        const emitter = new NativeEventEmitter(AppNavigate)
            emitter.addListener(
            "AndroidMsg", (event) => {

                console.log( "Event, key1=" + event.key1 )
            },
            null
        )
    }

    componentWillUnmount() {
        console.log("Unmounted")
    }
}

AppRegistry.registerComponent(
    'DashboardComponentRnApp',
    () => DashboardComponent
)


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


