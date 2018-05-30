import React from 'react';
import {SafeAreaView} from 'react-native';
import Navigator from './config/routes';

export default () => (
    <SafeAreaView style={{flex: 1}}>
        <Navigator />
    </SafeAreaView>
);