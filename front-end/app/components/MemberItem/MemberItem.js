import React from 'react';
import { TouchableOpacity, View, Text } from 'react-native';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <TouchableOpacity onPress={() => {this.props.onItemClick(this.props.item)}} style={styles.container}>
                <View style={styles.leftSide}>
                    <Text style={[styles.name, { color: this.name_color() }]}>{this.props.item.first_name} {this.props.item.last_name}</Text>
                </View>
                <View style={styles.rightSide}>
                    <Text style={[styles.price, { color: this.balance_color() }]}>{this.props.item.balance}€</Text>
                </View>
            </TouchableOpacity>
        );
    }

    name_color() {
        // according to the cotisation > balance
        let textDate = this.props.item.last_membership_payment.replace(" ", "T");
        let dateMembership = new Date(textDate);
        dateMembership.setFullYear(dateMembership.getFullYear() + 1);

        let dateNow = new Date();

        if(this.props.item.is_former_staff)
            return '#FBBC05';
        if(dateMembership.getTime() <= dateNow.getTime())
            return '#1E1E1E';
        if (parseFloat(this.props.item.balance) < parseFloat(this.props.limitBalance))
            return '#ba3f36';        
        return '#31978b';
    }

    balance_color() {
        // according to the balance
        if (this.props.item.balance <= this.props.limitBalance)
            return '#ba3f36';
        else
            return '#31978b';
    }
}