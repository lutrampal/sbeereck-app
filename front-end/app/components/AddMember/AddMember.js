import React from 'react';
import { TouchableOpacity, View, Text, TextInput, Picker } from 'react-native';
import styles from './styles';

export default class Header extends React.Component {
    render() {
        return (
            <View style={{flex: 1}}>
                <Text style={styles.addTitle}>Créer un nouveau membre</Text>
                <TextInput underlineColorAndroid='rgba(0,0,0,0)' autoCorrect={false} style={styles.textInput} placeholder="Prénom" value={this.props.newMemberFirstName} onChangeText={this.props.onEditFirstName} />
                <TextInput underlineColorAndroid='rgba(0,0,0,0)' autoCorrect={false} style={styles.textInput} placeholder="Nom de famille" value={this.props.newMemberLastName} onChangeText={this.props.onEditLastName} />
                <TextInput underlineColorAndroid='rgba(0,0,0,0)' keyboardType="phone-pad" style={styles.textInput} placeholder="Téléphone" value={this.props.newMemberPhone} onChangeText={this.props.onEditPhone} />
                <TextInput underlineColorAndroid='rgba(0,0,0,0)' autoCorrect={false} keyboardType="email-address" style={styles.textInput} placeholder="Adresse email" value={this.props.newMemberEmail} onChangeText={this.props.onEditEmail} />
                <Text style={styles.line}>École</Text>
                <Picker
                    style={styles.addType}
                    selectedValue={this.props.newMemberSchool}
                    onValueChange={(value, index) => this.props.onEditSchool(value)}>
                    <Picker.Item label="Ense3" value="Ense3" />
                    <Picker.Item label="Ensimag" value="Ensimag" />
                    <Picker.Item label="Esisar" value="Esisar" />
                    <Picker.Item label="GI" value="GI" />
                    <Picker.Item label="Papet" value="Papet" />
                    <Picker.Item label="Phelma" value="Phelma" />
                    <Picker.Item label="CPP" value="CPP" />
                    <Picker.Item label="Autre" value="Autre" />
                </Picker>

                <TouchableOpacity onPress={() => this.props.onAddPress()} style={styles.validateButton}>
                    <Text style={styles.validateText}>Sauvegarder</Text>
                </TouchableOpacity>
            </View>
        );
    }
}