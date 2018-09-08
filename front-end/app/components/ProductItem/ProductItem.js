import React from 'react';
import { TouchableOpacity, View, Text } from 'react-native';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <TouchableOpacity onPress={() => {this.props.onItemClick(this.props.item)}} style={styles.bottomContainer}>
                <View style={styles.leftSide}>
                    <Text style={[styles.name, {color: this.category()}]}>{this.props.item.name}</Text>
                </View>
                <View style={styles.rightSide}>
                    <Text style={[styles.price, { color: this.category() }]}>{this.props.item.price}€{this.unity()}</Text>
                </View>
            </TouchableOpacity>
        );
    }

    unity() {
        if (this.props.item.type === 'beer')
            return "/demi";
        return "";
    }

    category() {
        switch (this.props.item.type) {
            case 'beer': 
                return '#EDAF36';
            case 'food':
                return '#C586C0'
            default: 
                return '#31978B';
        }
    }
}