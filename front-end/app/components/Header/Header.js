import React from 'react';
import PropTypes from 'prop-type';
import { TouchableOpacity, View, Text, Keyboard } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return(
            <View style={styles.headerBar}>
                <TouchableOpacity
                    onPress={() => {this.props.leftButtonAction()}}
                    style={styles.leftButtonStyle}
                >
                    <Icon
                        name={this.props.leftButtonIcon}
                        size={30}
                        color="#FFF"
                        style={styles.leftIconStyle}
                    />
                </TouchableOpacity>
                <Text style={styles.textStyle}>{this.props.title}</Text>

                <TouchableOpacity
                    onPress={() => {this.props.rightButtonAction(); Keyboard.dismiss()}}
                    style={styles.rightButtonStyle}
                >
                    <Icon
                        name={this.props.rightButtonIcon}
                        size={30}
                        color="#FFF"
                        style={styles.rightIconStyle}
                    />
                </TouchableOpacity>
            </View>
        );
    }
}