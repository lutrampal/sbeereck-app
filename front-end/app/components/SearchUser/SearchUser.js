import React from 'react';
import PropTypes from 'prop-type';
import { TouchableOpacity, View, Text, TextInput, Keyboard, Picker, Dimensions } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import DatePicker from 'react-native-datepicker';
import styles from './styles';
import {SelectNumber} from '../SelectNumber';
import Autocomplete from 'react-native-autocomplete-input';

export default class Header extends React.Component {
    render() {
        return (
            <View style={{ flex: 1, width: '100%', zIndex: 1,  height: Dimensions.get('window').height-100, padding: 10}}>
                <Text style={styles.addTitle}>Membre</Text>
                    <Autocomplete
                        underlineColorAndroid='rgba(0,0,0,0)'
                        autoCapitalize="none"
                        style={{ zIndex: 1, backgroundColor: '#eff0f1', height: 'auto', padding: 0}}
                        autoCorrect={false}
                        inputContainerStyle={styles.inputContainerStyle}
                        containerStyle={styles.containerStyle}
                        data={this.props.data}
                        defaultValue={this.props.defaultValue}
                        onChangeText={this.props.onChangeText}
                        placeHolder={this.props.placeHolder}
                        renderItem={(item) => (
                            <TouchableOpacity onPress={() => this.props.onItemPress(item)}>
                                <Text style={styles.itemText}>
                                    {item.first_name} {item.last_name}
                                </Text>
                            </TouchableOpacity>
                        )}
                    />
            </View>
        );
    }

    getUserBalance() {
        if (this.props.selectedMember != null && this.props.selectedMember.id != undefined) {
            return (
                <View style={{ alignItems: 'center', width: "auto", height: "auto", padding: 5}}>
                    <Text style={{fontSize: 22}}>Solde</Text>
                    {this.balance(this.props.selectedMember.balance)}
                </View>
            )
        }
    }

    balance(balance) {
        return (
            <Text style={[{fontSize: 22}, { color: this.balance_color(balance) }]}>{parseFloat(balance).toFixed(2).toString()}€</Text>
        )
    }

    balance_color(balance) {
        // according to the balance
        if (parseFloat(balance) <= parseFloat(this.props.limitBalance))
            return '#ba3f36';
        else
            return '#31978b';
    }
}