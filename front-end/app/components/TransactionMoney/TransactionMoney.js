import React from 'react';
import PropTypes from 'prop-type';
import { TouchableOpacity, View, Text, TextInput, Keyboard, FlatList, Picker } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import DatePicker from 'react-native-datepicker';
import styles from './styles';
import { SelectNumber } from '../SelectNumber';
import { RadioGroup, RadioButton } from 'react-native-flexi-radio-button'

export default class Header extends React.Component {
    render() {
        return (
            <View style={{zIndex: 0, width: '100%', flexDirection: 'column', height: 'auto', padding: 10}}>
                <View style={{ flexDirection: 'row' }}>
                    <SelectNumber
                        style={{width: 150, height: 70, marginRight: 10}}
                        priceValue={this.props.moneyCount}
                        onChangeText={(text) => {let editedText = this.setPrice(text); this.props.setMoneyCount(editedText)}}
                        onMorePress={() => { this.props.setMoneyCount(((parseFloat(this.props.moneyCount)+0.01)).toFixed(2).toString()) }}
                        onLessPress={() => { if (parseFloat(this.props.moneyCount).toFixed(2) > 0) this.props.setMoneyCount((parseFloat(this.props.moneyCount) - 0.01).toFixed(2).toString()) }} />
                </View>

                <TextInput
                    underlineColorAndroid='rgba(0,0,0,0)' 
                    onChangeText={this.props.setMoneyName} 
                    value={this.props.moneyName} 
                    placeHolder="Description de la transaction" 
                    onEndEditing={() => { Keyboard.dismiss }}
                    style={{
                        marginTop: 20,
                        fontSize: 18,
                        height: 35,
                        padding: 7,
                        backgroundColor: '#eff0f1',
                        borderRadius: 7,
                    }}/>

            </View>
        );
    }

    setPrice(text) {
        if (text == "" || text == "0")
            return "";
        else
            return text.replace(",", ".");
    }
}