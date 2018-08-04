import React from 'react';
import { TouchableOpacity, View, Text} from 'react-native';
import styles from './styles';
import {SelectNumber} from '../SelectNumber';

export default class Header extends React.Component {
    render() {
        return (
            <View style={{flex: 1}}>
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
                <TouchableOpacity onPress={() => this.props.onUpdateBalancePress(this.props.viewItem)} style={styles.validateButton}>
                    <Text style={styles.validateText}>Mettre à jour la balance</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={() => this.props.onCloseAccountPress(this.props.viewItem)} style={styles.suppressButton}>
                    <Text style={styles.validateText}>Clôturer le compte</Text>
                </TouchableOpacity>
            </View>
        );
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
}