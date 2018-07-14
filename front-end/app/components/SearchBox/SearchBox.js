import React from 'react';
import { View, TextInput, Keyboard } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <View style={[styles.container, this.showInput()]}>
                <View style={[styles.textContainer]}>
                    <Icon
                        name="search"
                        size={18}
                        color="#000"
                        style={styles.leftIconStyle}
                    />
                    <TextInput
                        underlineColorAndroid='rgba(0,0,0,0)' 
                        autoCorrect={false}
                        style={styles.textInput}
                        onChangeText={(text) => {this.props.editText(text)}}
                        value={this.props.searchText}
                        placeholder="Recherche"
                        onEndEditing={() => {Keyboard.dismiss}}
                    />
                </View>
            </View>
        );
    }

    showInput() {
        if(!this.props.showBox) {
            return {display: 'none'};
        } else {

            return null;
        }
    }
}