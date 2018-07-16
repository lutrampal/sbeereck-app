import React from 'react';
import {View, Text, TextInput} from 'react-native';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <View style={{width: '100%', height: 'auto', padding: 10}}>
                <Text style={styles.addTitle}>Authentification</Text>
                <TextInput underlineColorAndroid='rgba(0,0,0,0)' autoCorrect={false}
                           keyboardType='url' style={styles.textInput}
                           onChangeText={this.props.editAppHost}
                           value={this.props.appHost} placeholder="Serveur"/>
                <TextInput underlineColorAndroid='rgba(0,0,0,0)' secureTextEntry={true}
                           style={[styles.textInput, {
                               borderBottomWidth: 1,
                               borderColor: 'grey'
                           }]} onChangeText={this.props.editAppToken}
                           value={this.props.appToken} placeholder="Mot de passe"/>
            </View>
        );
    }
}
