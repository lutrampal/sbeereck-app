import React from 'react';
import {TouchableOpacity, View, Text, TextInput, Picker} from 'react-native';
import styles from './styles';
import {SelectNumber} from '../SelectNumber';

export default class Header extends React.Component {

    render() {
        return (
            <View>
                {this.getMemberView()}
            </View>
        );
    }it() {
    }

    getPhone() {
        if(this.props.phone != null)
        {
            return (<Text style={styles.line}>Téléphone : {this.props.phone}</Text>)
        }
    }

    getMembership() {
        let textDate = this.props.viewItem.last_membership_payment.replace(" ", "T");
        let dateMembership = new Date(textDate);
        dateMembership.setFullYear(dateMembership.getFullYear() + 1);

        let dateNow = new Date();

      let dd = dateMembership.getDate()
      let mm = dateMembership.getMonth() + 1
      const yyyy = dateMembership.getFullYear()
      if (dd < 10) {
            dd = '0' + dd;
        }
        if (mm < 10) {
            mm = '0' + mm;
        }
        formatedDate = dd + '/' + mm + '/' + yyyy;

        if(this.props.viewItem.is_former_staff)
            return (<Text style={[styles.line, { color: "#FBBC05" }]}>Cotisation à jour (ancien staff)</Text>)
        if (dateMembership.getTime() <= dateNow.getTime())
            return (<Text style={[styles.line, { color: "#1E1E1E" }]}>Cotisation expirée ({formatedDate})</Text>)
        return (<Text style={[styles.line, { color: "#31978b" }]}>Cotisation à jour ({formatedDate})</Text>)
    }

    getCotisation() {
        if(this.props.canUpdateMembership)
            return (
                <TouchableOpacity onPress={() => this.props.onUpdateMembershipPress(this.props.viewItem)} style={styles.validateButton}>
                    <Text style={styles.validateText}>Renouveler la cotisation</Text>
                </TouchableOpacity>
            );
        else return (<View />);
    }

    getMemberView() {
        if (!this.props.editing) {
            return this.getInfoView()
        } else {
            return this.getEditView()
        }
    }

    getInfoView() {
        return <View style={{flex: 1}}>
            <Text style={styles.addTitle}>{this.props.viewItem.first_name} {this.props.viewItem.last_name}</Text>

            <Text style={styles.line}>Balance</Text>
            <SelectNumber
                priceValue={this.props.balanceValue.toString()}
                onChangeText={(value) => this.props.onChangeBalance(value)}
                onMorePress={() => this.props.onMorePress()}
                onLessPress={() => this.props.onLessPress()} />
            <Text style={styles.line}>Email : {this.props.email}</Text>
            <Text style={styles.line}>École : {this.props.school}</Text>
            {this.getPhone()}
            {this.getMembership()}
            {this.getCotisation()}
            <TouchableOpacity onPress={() => this.props.onShowTransactionsPress(this.props.viewItem)}
                              style={styles.validateButton}>
                <Text style={styles.validateText}>Voir les transactions</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => this.props.onEditPress(this.props.viewItem)}
                              style={styles.validateButton}>
                <Text style={styles.validateText}>Editer</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => this.props.onUpdateBalancePress(this.props.viewItem)}
                              style={styles.validateButton}>
                <Text style={styles.validateText}>Mettre à jour la balance</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => this.props.onCloseAccountPress(this.props.viewItem)}
                              style={styles.suppressButton}>
                <Text style={styles.validateText}>Clôturer le compte</Text>
            </TouchableOpacity>
        </View>

    }

    getEditView() {
        return (<View style={{flex: 1}}>
            <Text style={styles.addTitle}>Editer {this.props.viewItem.first_name} {this.props.viewItem.last_name}</Text>

            <TextInput autoCorrect={false} style={styles.editableLine} defaultValue={this.props.viewItem.first_name}
                       placeholder="Prénom" onChangeText={this.props.onEditFirstName}/>
            <TextInput autoCorrect={false} style={styles.editableLine} defaultValue={this.props.viewItem.last_name}
                       placeholder="Nom" onChangeText={this.props.onEditLastName}/>
            <TextInput autoCorrect={false} keyboardType='email-address' style={styles.editableLine}
                       defaultValue={this.props.email} placeholder="Email" onChangeText={this.props.onEditEmail}/>
            <TextInput autoCorrect={false} keyboardType='phone-pad' style={styles.editableLine} defaultValue={this.props.phone}
                       placeholder="Téléphone" onChangeText={this.props.onEditPhone}/>
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
            <View>
                <TouchableOpacity onPress={() => this.props.onValidateEditPress(this.props.viewItem)}
                                  style={styles.validateButton}>
                    <Text style={styles.validateText}>Valider</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={() => this.props.onCancelEditPress()}
                                  style={styles.suppressButton}>
                    <Text style={styles.validateText}>Annuler</Text>
                </TouchableOpacity>
            </View>
        </View>)
    }
}