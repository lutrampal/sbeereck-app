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
                        priceValue={this.props.foodsCount.toString()}
                        onChangeText={(value) => { return; }}
                        disableChangeText={true}
                        onMorePress={() => { this.props.setFoodsCount(parseInt(this.props.foodsCount, 10)+1) }}
                        onLessPress={() => { if (parseInt(this.props.foodsCount, 10) > 1) this.props.setFoodsCount(parseInt(this.props.foodsCount, 10) - 1) }} />
                </View>

                <FlatList
                    data={this.props.foods}
                    extraData={this.props}
                    keyExtractor={(item, index) => index.toString()}
                    renderItem={({ item }) => (
                            <TouchableOpacity style={{flexDirection: 'row', height: 'auto', paddingTop: 10, paddingBottom: 10, borderBottomWidth: 1, borderColor: 'grey'}} onPress={() => this.props.setSelectedFood(item)}>
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

    isSelected(food) {
        if(food.id == this.props.selectedFood.id)
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
}