import React from 'react';
import PropTypes from 'prop-type';
import { TouchableOpacity, View, Text, TextInput, Keyboard, Picker } from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import DatePicker from 'react-native-datepicker';
import styles from './styles';
import { SelectNumber } from '../SelectNumber';
import { RadioGroup, RadioButton } from 'react-native-flexi-radio-button'

export default class Header extends React.Component {
    render() {
        return (
            <View style={{zIndex: 0, width: '100%', height: 'auto', padding: 10}}>
                <RadioGroup
                    style={{ flexDirection: 'column' }}
                    color='black'
                    selectedIndex={this.selectedIndex()}
                    onSelect={(index, value) => this.props.changeSelectedCategory(value)}
                >
                    <RadioButton value={'beer'} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5}} activeColor='#fbbc05'>
                         <Text style={{fontSize: 18}}>Bière</Text>
                    </RadioButton>

                    <RadioButton value={'deposit'} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5}} activeColor='#fbbc05'>
                        <Text style={{ fontSize: 18 }}>Caution</Text>
                    </RadioButton>

                    <RadioButton value={'food'} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5 }} activeColor='#fbbc05'>
                        <Text style={{ fontSize: 18 }}>Nourriture</Text>
                    </RadioButton>

                    <RadioButton value={'money'} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5 }} activeColor='#fbbc05'>
                        <Text style={{ fontSize: 18 }}>Recharge / autres</Text>
                    </RadioButton>
                </RadioGroup>
            </View>
        );
    }

    selectedIndex() {
        switch (this.props.selectedCategory) {
            case 'money':
                return 3;
            case 'food':
                return 2;
            case 'deposit':
                return 1;
            default:
                return 0;
        }
    }
}