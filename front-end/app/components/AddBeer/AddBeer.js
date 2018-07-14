import React from 'react';
import {Picker, Text, TouchableOpacity, View} from 'react-native';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <View style={{flex: 1}}>
                <Text style={styles.addTitle}>Ajouter une bière</Text>
                <Picker
                    style={styles.addType}
                    selectedValue={this.props.selectedBeerAddId}
                    onValueChange={this.props.onBeerChange}>

                    {this.props.availableBeers.map((data) => {
                        return (
                            <Picker.Item label={data.label} value={data.value} />
                        )
                    })}
                </Picker>
                
                
                <TouchableOpacity onPress={this.props.onValidatePress} style={styles.validateButton}>
                    <Text style={styles.validateText}>Ajouter</Text>
                </TouchableOpacity>
            </View>
        );
    }
}