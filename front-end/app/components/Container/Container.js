import React from 'react';
import PropTypes from 'prop-types';
import {StatusBar, SafeAreaView} from 'react-native';

import styles from './styles';

const Container = ({children}) => (
        <SafeAreaView style={styles.bottomContainer}>
            <StatusBar hidden={true} translucent={true} barStyle="default" />
            {children}
        </SafeAreaView>
);

Container.propTypes = {
    children: PropTypes.any,
};

export default Container;