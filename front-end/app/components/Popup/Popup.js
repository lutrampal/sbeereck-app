import React from 'react';
import {View, ScrollView, TouchableWithoutFeedback, Keyboard} from 'react-native';

import styles from './styles';

export default class Header extends React.Component {
    render() {
        return this.shown();
    }

    shown() {
        if(this.props.shown)
        {
            return (<View style={[{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0, alignItems: 'center', justifyContent: 'center', paddingTop: 20, paddingBottom: 20 }]} >
                <TouchableWithoutFeedback onPress={() => { Keyboard.dismiss(); this.props.toggleState() }}>
                    <View style={styles.bottomContainer}/>
                </TouchableWithoutFeedback>
                <View style={styles.box}>
                    <ScrollView alwaysBounceVertical={false} style={{ width: 'auto', height: 'auto' }}>
                        {this.props.children}
                    </ScrollView>
                </View>
            </View>)
        }
        else
            return (<View/>)
    }
}