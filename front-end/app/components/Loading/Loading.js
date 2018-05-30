import React from 'react';
import PropTypes from 'prop-types';
import {View, StatusBar, ActivityIndicator, TouchableWithoutFeedback} from 'react-native';

import styles from './styles';

export default class Header extends React.Component {
    render() {
        return this.shown();
    }

    shown() {
        if(this.props.shown != true)
            return (<View></View>);
        else
            return (
                <View style={[{ position: 'absolute', top: 0, left: 0, right: 0, bottom: 0, alignItems: 'center', justifyContent: 'center', paddingTop: 20, paddingBottom: 20 }]}>
                    <View style={styles.container}></View>
                    <View style={styles.box}>
                        <ActivityIndicator size="large" color="#edaf36" />
                    </View>
                </View>
            );
    }
}