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
                        priceValue={this.props.depositsCount.toString()}
                        onChangeText={(value) => { return; }}
                        disableChangeText={true}
                        onMorePress={() => { this.props.setDepositsCount(parseInt(this.props.depositsCount, 10)+1) }}
                        onLessPress={() => { if (parseInt(this.props.depositsCount, 10) > 1) this.props.setDepositsCount(parseInt(this.props.depositsCount, 10) - 1) }} />

                    <RadioGroup
                        style={{ flexDirection: 'column' }}
                        color='black'
                        selectedIndex={this.selectedIndex()}
                        onSelect={(index, value) => this.props.changeSelectedType(value)}
                    >
                        <RadioButton value={-1} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5}} activeColor='#fbbc05'>
                            <Text style={{fontSize: 18}}>Encaisser</Text>
                        </RadioButton>

                        <RadioButton value={1} color="#fbbc05" style={{ padding: 0, paddingLeft: 5, paddingRight: 5, borderColor: '#fbbc05', marginBottom: 5}} activeColor='#fbbc05'>
                            <Text style={{ fontSize: 18 }}>Rendre</Text>
                        </RadioButton>
                    </RadioGroup>
                </View>

                <FlatList
                    data={this.props.deposits}
                    extraData={this.props}
                    keyExtractor={(item, index) => index.toString()}
                    renderItem={({ item }) => (
                            <TouchableOpacity style={{flexDirection: 'row', height: 'auto', paddingTop: 10, paddingBottom: 10, borderBottomWidth: 1, borderColor: 'grey'}} onPress={() => this.props.setSelectedDeposit(item)}>
                                <Text style={{flex: 1, fontSize: 20, fontWeight: 'bold', color: 'black'}}>
                                    {item.name} ({parseFloat(item.price).toFixed(2)}€)
                                </Text>
                                {this.isSelected(item)}
                            </TouchableOpacity>
                        )
                    }
                />

            </View>
        );
    }

    isSelected(deposit) {
        if(deposit.id == this.props.selectedDeposit.id)
        {
            return (
                <Icon
                    name="check"
                    color="#fbbc05"
                    size={20}
                />
            )
        }
    }

    selectedIndex() {
        switch (this.props.depositType) {
            case -1:
                return 0;
            case 1:
                return 1;
        }
    }
}