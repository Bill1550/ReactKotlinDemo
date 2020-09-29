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

import ReactNativeActionToShell from './ReactNativeActionToShell'
import  ActionSheet  from './ReactNativeActionSheet'
import { getManufacturer, getModel } from "react-native-device-info";

var staticDataItem = '-- empty --';
var helloCounter = 0;  // some static data that we will modify.

let actionSheetOptions = ['Option one', 'Option two', 'Option three']

class HelloWorld extends  React.Component {
    constructor(props) {

        super(props);
        this.state = { uiTheme: props.uiTheme, screen: props.screen };

        // Test react-native-device-info lib
        console.log( "Device model: " + getModel() );
        getManufacturer().then(manufacturer => { console.log( "Manufacturer-" + manufacturer.toString() ) } );
    }

    render() {
        let activeStyle = (this.state.uiTheme === 'Dark') ? darkStyles : lightStyles;
        helloCounter++;

        return (


            <View style={activeStyle.container}>
                <Text style={activeStyle.hello}>Hello from React Native!</Text>
                <Text style={activeStyle.subhead}>Counter: {helloCounter}</Text>
                <Text style={activeStyle.subhead}>Screen: {this.state.screen}</Text>
                <Text style={activeStyle.subhead}>ApiKey: {this.props.apiKey}</Text>
                <Text style={activeStyle.tiny}>JWT: {this.props.jwt}</Text>
                <Button
                    onPress={() => {
                        console.log("requesting nav to /book")
                        ReactNativeActionToShell.handleAction( { type: "url", url: "/book" } )
                    }}
                    title={"Navigate to Get Care"}
                />
                <Text style={activeStyle.subhead}>-------</Text>
                <Button
                    onPress={() => {
                        console.log("requesting nav to /about")
                        ReactNativeActionToShell.handleAction( { type: "url", url: "/about" } )
                    }}
                    title={"Show About"}
                />
                <Text style={activeStyle.subhead}>-------</Text>
                <Button
                    onPress={() => {
                        console.log("opening action sheet")
                        ActionSheet.showActionSheetWithOptions(
                            {
                                options: actionSheetOptions,
                                cancelButtonIndex: 0,
                                destructiveButtonIndex: 1,
                                tintColor: 'blue'
                            },
                            (buttonIndex) => {
                                console.log('Action sheet clicked:', buttonIndex )
                            }
                        )
                    }}
                    title={"Action!"}
                />
            </View>
        );
    }

    componentDidMount() {
        console.log("HelloWorld Component Mounted")
        const emitter = new NativeEventEmitter(ReactNativeActionToShell)
        emitter.addListener(
            "AndroidMsg", (event) => {

                console.log( "Event, type=" + event.type );
                if ( event.type === "uiTheme" )
                    this.setState({ uiTheme: event.uiTheme, screen: this.state.screen });

                if ( event.type === "screen") {
                    console.log( "Screen event: " + event.screen )
                    this.setState({uiTheme: this.state.uiTheme, screen: event.screen});
                }
            },
            null
        )
    }

    componentWillUnmount() {
        console.log("Unmounted")
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
        let styles = darkStyles
        return (
            <View style={styles.container}>
                <Text style={styles.hello}>Hello Fragment 2!</Text>
                <Button
                    onPress={() => {
                        console.log("requesting nav to /book")
                        ReactNativeActionToShell.handleAction({ type: "url", url: "/home"})
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
        let styles = darkStyles
        return (
            <View style={styles.container}>
                <Text style={styles.hello}>Dashboard Fragment</Text>
                <Text style={styles.hello}>{staticDataItem}</Text>
                <Button
                    onPress={() => {
                        console.log("requesting nav to /book")
                        ReactNativeActionToShell.handleAction({ type: "url", url: "/home" } )
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
        const emitter = new NativeEventEmitter(ReactNativeActionToShell)
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
    () => DashboardrComponent
)


const lightStyles = StyleSheet.create({
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
        margin: 10,
        color: 'blue'
    },
    tiny: {
        fontSize: 10,
        textAlign: 'center',
        margin: 10,
        color: 'blue'
    }
});

const darkStyles = StyleSheet.create({
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
        margin: 10,
        color: 'white'
    },
    subhead: {
        fontSize: 16,
        textAlign: 'center',
        margin: 10,
        color: 'white'
    },
    tiny: {
        fontSize: 10,
        textAlign: 'center',
        margin: 10,
        color: 'blue'
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
    'AppContainer',
    () => HelloWorld
)

