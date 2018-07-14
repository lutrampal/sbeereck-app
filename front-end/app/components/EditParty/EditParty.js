import React from 'react';
import { TouchableOpacity, View, Text, TextInput, Keyboard} from 'react-native';
import Icon from 'react-native-vector-icons/FontAwesome';
import DatePicker from 'react-native-datepicker';
import styles from './styles';
import {SelectNumber} from '../SelectNumber';

export default class Header extends React.Component {
    render() {
        return (
            <View style={{flex: 1}}>
                <Text style={styles.addTitle}>{this.props.item.name}</Text>
                <TextInput
                    underlineColorAndroid='rgba(0,0,0,0)' 
                    autoCorrect={false}
                    style={styles.addName}
                    onChangeText={this.props.onChangePartyName}
                    value={this.props.editPartyName}
                    placeholder="Nom"
                    onEndEditing={() => { Keyboard.dismiss }}
                />
                <DatePicker
                    style={styles.datePicker}
                    date={this.props.editPartyDate}
                    mode="date"
                    placeholder="Date"
                    format="YYYY-MM-DD"
                    minDate="2018-04-06"
                    maxDate="2100-02-14"
                    confirmBtnText="Confirmer"
                    cancelBtnText="Annuler"
                    customStyles={{
                        dateIcon: {
                            position: 'absolute',
                            left: 0,
                            top: 4,
                            marginLeft: 0
                        },
                        dateInput: {
                            marginLeft: 36,
                            borderWidth: 0,
                            borderRadius: 7,
                        },
                        placeholderText: {
                            fontSize: 18,
                            textAlign: 'left',
                        }, 
                        dateText: {
                            fontSize: 18,
                            textAlign: 'left',
                        }
                        // ... You can check the source to find the other keys. 
                    }}
                    onDateChange={this.props.onDateChange}
                />
                
                <View style={styles.beersBox}>
                    <View style={styles.normals}>
                        <Text style={styles.normalTitle}>Demi bières normales</Text>
                        <SelectNumber
                            priceValue={this.props.normalPriceValue.toString()}
                            onChangeText={this.props.onChangeNormalPrice}
                            onMorePress={this.props.onNormalMorePress}
                            onLessPress={this.props.onNormalLessPress} />

                    </View>
                    <View style={styles.specials}>
                        <Text style={styles.specialTitle}>Demi bières spéciales</Text>
                        <SelectNumber
                            priceValue={this.props.specialPriceValue.toString()}
                            onChangeText={this.props.onChangeSpecialPrice}
                            onMorePress={this.props.onSpecialMorePress}
                            onLessPress={this.props.onSpecialLessPress} />

                    </View>
                </View>

                <TouchableOpacity onPress={this.props.onEditBeersPress}>
                    <Text style={styles.addBeers}>
                        <Icon
                            name="plus-circle"
                            size={18}
                        /> Les bières servies
                    </Text>
                </TouchableOpacity>
                
                <TouchableOpacity onPress={() => this.props.onSuppressPress(this.props.item)} style={styles.suppressButton}>
                    <Text style={styles.validateText}>Supprimer</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={() => this.props.onValidatePress(this.props.item)} style={styles.validateButton}>
                    <Text style={styles.validateText}>Sauvegarder</Text>
                </TouchableOpacity>
            </View>
        );
    }
}