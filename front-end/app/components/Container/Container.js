import React from 'react';
import PropTypes from 'prop-types';
import {View, StatusBar, SafeAreaView, TouchableWithoutFeedback, Keyboard} from 'react-native';

import styles from './styles';

const Container = ({children}) => (
        <SafeAreaView style={styles.container}>
            <StatusBar hidden={true} translucent={true} barStyle="default" />
            {children}
        </SafeAreaView>
);

Container.propTypes = {
    children: PropTypes.any,
};

export default Container;