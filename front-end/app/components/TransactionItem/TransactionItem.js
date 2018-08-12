import React from 'react';
import { TouchableOpacity, View, Text } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import styles from './styles';
import Toast from "react-native-simple-toast"

export default class Header extends React.Component {
    render() {
        return (
            <View style={styles.bottomContainer}>
                <TouchableOpacity style={styles.leftSide} onPress={() => Toast.show(this.props.item.timestamp)}>
                    <Text style={[styles.name, { fontWeight: 'bold' }]}>{this.props.item.label}</Text>
                    <Text style={styles.name}>{this.props.bottom_text}</Text>
                </TouchableOpacity>
                <View style={styles.rightSide}>
                    <Text style={[styles.price, { color: this.transaction_color() }]}>{this.transaction_price()}€</Text>
                    <TouchableOpacity onPress={() => { this.props.onClickDelete(this.props.item) }} style={styles.editButton}>
                        <Icon
                            color="white"
                            name="trash"
                            size={15}
                        />
                    </TouchableOpacity>
                </View>
            </View>
        );
    }

    transaction_color() {
        // according to the balance
        if (this.props.item.amount < 0)
            return '#FC1D1E';
        else
            return '#90C652';
    }

    transaction_price() {
        // according to the balance
        if (this.props.item.amount < 0)
            return parseFloat(this.props.item.amount).toFixed(2).toString();
        else
            return '+' + parseFloat(this.props.item.amount).toFixed(2).toString();
    }
}