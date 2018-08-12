import React from 'react';
import {View, Text, FlatList, Keyboard, TouchableOpacity, AsyncStorage, Alert} from 'react-native';
import EStyleSheet from 'react-native-extended-stylesheet';
import {Container} from '../components/Container';
import {Header} from '../components/Header';
import {MemberItem} from '../components/MemberItem';
import {YellowButton} from '../components/YellowButton';
import {SearchBox} from '../components/SearchBox';
import {Popup} from '../components/Popup';
import {Loading} from '../components/Loading';
import {ViewMember} from '../components/ViewMember';
import {AddMember} from '../components/AddMember';
import * as RNFS from "react-native-fs"
import {open} from "react-native-share"
import Toast from "react-native-simple-toast"

EStyleSheet.build({
    $mainBackground: '#F9F9F9'
})

export default class Home extends React.Component {
    static navigationOptions = {
        drawerLabel: "Adhérents",
    }

    constructor(props) {
        super(props);
        this.state = {
            newMemberFirstName: "",
            newMemberLastName: "",
            newMemberEmail: "",
            newMemberSchool: "",
            newMemberPhone: "",

            editMemberBalance: "0.00",

            appHost: "",
            appToken: "",

            isSearching: false,
            searchValue: "",

            viewItem: {last_membership_payment: ""},

            addPopup: false,
            viewPopup: false,

            initialMembers: [],
            members: [],

            loading: false,
            editingMember: false
        };
    }

    async componentDidMount() {
        this.setState({loading: true});

        try {
            let host = await AsyncStorage.getItem('@SbeerEck:host');
            const token = await AsyncStorage.getItem('@SbeerEck:token');

            if (host != null) {
                host = host.replace(" ", "");
                this.setState({appHost: host});
            }
            if (token != null)
                this.setState({appToken: token});

            await this.checkConnection();

            if (this.state.connected) {
                response = await fetch('https://' + this.state.appHost + '/balance_too_low_threshold',
                    {
                        headers: {
                            'authentication-token': this.state.appToken
                        }
                    });

                // noinspection JSUndeclaredVariable
                request = await response.json();
                let balance_treshold = request.balance_too_low_threshold;
                this.setState({limitBalance: balance_treshold});

                this.initiateMembers();
            }
        } catch (error) {
            Toast.show("Erreur lors du chargement des paramètres.\n" + error);
            this.setState({loading: false});
        }
    }

    render() {
        return (
            <Container>
                <Header title="Adhérents" leftButtonIcon="menu" leftButtonAction={() => {
                    this.props.navigation.navigate('DrawerOpen');
                    Keyboard.dismiss()
                }} rightButtonAction={() => {
                    this.setState({
                        isSearching: !this.state.isSearching,
                        searchValue: "",
                        members: this.state.initialMembers
                    })
                }} rightButtonIcon="magnify"/>

                <SearchBox showBox={this.state.isSearching}
                           searchText={this.state.searchValue}
                           editText={(text) => {
                               this.setState(
                                   {
                                       searchValue: text,
                                       members: this.state.initialMembers.filter((line) => ((this.preg(line.first_name) + this.preg(line.last_name)).includes(this.preg(text))))
                                   }
                               );
                           }}/>

                <View style={{flexDirection: 'row', height: 'auto', width: '100%', padding: 5}}>
                    <Text style={{fontSize: 16}}>Trier par... </Text>
                    <TouchableOpacity onPress={() => {
                        this.state.members.sort(this.compareLastName);
                        this.setState({});
                    }}>
                        <Text style={{fontSize: 16}}>Nom de famille |</Text>
                    </TouchableOpacity>

                    <TouchableOpacity onPress={() => {
                        this.state.members.sort(this.compareBalance);
                        this.setState({});
                    }}>
                        <Text style={{fontSize: 16}}> Balance</Text>
                    </TouchableOpacity>
                </View>

                <FlatList
                    refreshing={false}
                    onRefresh={this.initiateMembers.bind(this)}
                    data={this.state.members}
                    extraData={this.state}
                    keyExtractor={(item, index) => index.toString()}
                    renderItem={({item}) => <MemberItem item={item} limitBalance={this.state.limitBalance}
                                                        onItemClick={(theItem) => {
                                                            this.getMemberDetails(theItem)
                                                        }}/>}
                />
                <YellowButton buttonIcon="plus" buttonAction={() => {
                    this.setState({
                        addPopup: true,
                        newMemberFirstName: "",
                        newMemberLastName: "",
                        newMemberEmail: "",
                        newMemberSchool: "",
                        newMemberPhone: "",
                    })
                }}/>

                <YellowButton top={true} buttonIcon="table" buttonAction={() => {this.exportMembers()}}/>

                <Popup shown={this.state.viewPopup}
                       toggleState={() => this.setState({viewPopup: !this.state.viewPopup})}>
                    <ViewMember
                        viewItem={this.state.viewItem}
                        canUpdateMembership={this.canUpdateMembership()}

                        onMorePress={() => {
                            this.setState({editMemberBalance: (parseFloat(this.state.editMemberBalance) + 1).toFixed(2).toString()})
                        }}
                        onLessPress={() => {
                            this.setState({editMemberBalance: (parseFloat(this.state.editMemberBalance) - 1).toFixed(2).toString()})
                        }}
                        onChangeBalance={(text) => {
                            let editedText = this.setPrice(text);
                            this.setState({editMemberBalance: editedText})
                        }}
                        balanceValue={this.state.editMemberBalance}

                        phone={this.state.viewUserPhone}
                        email={this.state.viewUserEmail}
                        school={this.state.viewUserSchool}
                        editing={this.state.editingMember}

                        onUpdateMembershipPress={(item) => {
                            Alert.alert(
                                'Renouveler la cotisation',
                                'Êtes-vous sûr de vouloir renouveler la cotisation de ' + item.first_name + ' ' + item.last_name + ' ?',
                                [
                                    {
                                        text: 'Annuler', onPress: () => {
                                            this.setState({viewPopup: false})
                                        }, style: 'cancel'
                                    },
                                    {
                                        text: 'Renouveler', onPress: () => {
                                            this.setState({loading: true}); // TODO : add in transaction with the staff id
                                            this.updateMembership(item.id);
                                            this.setState({viewPopup: false})
                                        }
                                    },
                                ],
                                {cancelable: false}
                            )
                        }}

                        onShowTransactionsPress={(item) => {
                            this.showMemberTransactions(item)
                        }}

                        onUpdateBalancePress={(item) => {
                            Alert.alert(
                                'Modifier la balance ',
                                'Êtes-vous sûr de vouloir modifier la balance de ' + item.first_name + ' ' + item.last_name + ' ? \nL\'information sera traçée dans les logs avec votre nom.',
                                [
                                    {
                                        text: 'Annuler', onPress: () => {
                                            this.setState({viewPopup: false})
                                        }, style: 'cancel'
                                    },
                                    {
                                        text: 'Modifier', onPress: () => {
                                            this.setState({loading: true}); // TODO : add the transaction description with staff id
                                            this.editMemberBalance(item, this.state.editMemberBalance);
                                            this.setState({viewPopup: false})
                                        }
                                    },
                                ],
                                {cancelable: false}
                            )
                        }}


                        onValidateEditPress={(item) => {
                            this.setState({loading: true});
                            this.editMember(item.id, this.state.newMemberFirstName, this.state.newMemberLastName,
                                this.state.newMemberSchool, this.state.newMemberEmail, this.state.newMemberPhone);
                            this.setState({
                                viewPopup: false,
                                editingMember: false,
                                newMemberFirstName: "",
                                newMemberLastName: "",
                                newMemberEmail: "",
                                newMemberSchool: "",
                                newMemberPhone: ""
                            })
                        }}

                        onEditPress={(item) => {
                            this.setState({
                                editingMember: true,
                                newMemberFirstName: item.first_name,
                                newMemberLastName: item.last_name,
                                newMemberEmail: this.state.viewUserEmail,
                                newMemberSchool: this.state.viewUserSchool,
                                newMemberPhone: this.state.viewUserPhone,
                            })
                        }}

                        onCancelEditPress={() => {
                            this.setState({
                                editingMember: false,
                                newMemberFirstName: "",
                                newMemberLastName: "",
                                newMemberEmail: "",
                                newMemberSchool: "",
                                newMemberPhone: "",
                            })
                        }}

                        onEditFirstName={(text) => {
                            this.setState({newMemberFirstName: text})
                        }}
                        onEditLastName={(text) => {
                            this.setState({newMemberLastName: text})
                        }}
                        onEditEmail={(text) => {
                            this.setState({newMemberEmail: text})
                        }}
                        onEditSchool={(text) => {
                            this.setState({newMemberSchool: text})
                        }}
                        onEditPhone={(text) => {
                            this.setState({newMemberPhone: text})
                        }}

                        newMemberSchool={this.state.newMemberSchool}
                        onCloseAccountPress={(item) => {
                            Alert.alert(
                                'Clôture du compte',
                                'Êtes-vous sûr de vouloir fermer le compte de ' + item.first_name + ' ' + item.last_name + ' ?\nCette opération sera irréversible !',
                                [
                                    {
                                        text: 'Annuler', onPress: () => {
                                            this.setState({viewPopup: false})
                                        }, style: 'cancel'
                                    },
                                    {
                                        text: 'Clôturer', onPress: () => {
                                            this.setState({loading: true}); // TODO : add log with the staff id
                                            this.closeAccount(item.id);
                                            this.setState({viewPopup: false})
                                        }
                                    },
                                ],
                                {cancelable: false}
                            )
                        }}
                    />
                </Popup>


                <Popup shown={this.state.addPopup} toggleState={() => this.setState({addPopup: !this.state.addPopup})}>
                    <AddMember
                        onEditFirstName={(text) => {
                            this.setState({newMemberFirstName: text})
                        }}
                        onEditLastName={(text) => {
                            this.setState({newMemberLastName: text})
                        }}
                        onEditEmail={(text) => {
                            this.setState({newMemberEmail: text})
                        }}
                        onEditSchool={(text) => {
                            this.setState({newMemberSchool: text})
                        }}
                        onEditPhone={(text) => {
                            this.setState({newMemberPhone: text})
                        }}

                        newMemberEmail={this.state.newMemberEmail}
                        newMemberFirstName={this.state.newMemberFirstName}
                        newMemberLastName={this.state.newMemberLastName}
                        newMemberPhone={this.state.newMemberPhone}
                        newMemberSchool={this.state.newMemberSchool}

                        onAddPress={() => {
                            this.setState({loading: true});
                            this.createAccount(this.state.newMemberFirstName, this.state.newMemberLastName, this.state.newMemberPhone, this.state.newMemberSchool, this.state.newMemberEmail);
                            this.setState({addPopup: false})
                        }}
                    />
                </Popup>

                <Loading shown={this.state.loading}/>
            </Container>
        );
    }

    canUpdateMembership() {
        let textDate = this.state.viewItem.last_membership_payment.replace(" ", "T");
        let dateMembership = new Date(textDate);
        dateMembership.setFullYear(dateMembership.getFullYear() + 1);

        let dateNow = new Date();

        return dateMembership.getTime() <= dateNow.getTime();

    }

    async getMemberDetails(theItem) {
        this.setState({loading: true})
        try {
            let response = await fetch('https://' + this.state.appHost + '/members/' + theItem.id,
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let user = await response.json();

            this.setState({
                viewPopup: true,
                viewItem: theItem,
                editMemberBalance: theItem.balance,
                viewUserSchool: user.school,
                viewUserEmail: user.email,
                viewUserPhone: user.phone
            });

            this.setState({loading: false});

        } catch (error) {
            this.setState({loading: false});
            console.log(error);
        }
    }

    async initiateMembers() {
        await this.checkConnection();

        this.setState({loading: true});

        try {
            let response = await fetch('https://' + this.state.appHost + '/members',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let request = await response.json();

            this.setState({initialMembers: request});

            this.setState({members: this.state.initialMembers, loading: false, searchValue: ""});
        } catch (error) {
            this.setState({loading: false, searchValue: ""});
            console.log(error);
        }
    }

    async getFullMembers() {
        await this.checkConnection();

        this.setState({loading: true});

        try {
            let response = await fetch('https://' + this.state.appHost + '/members/full',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            let response_body = await response.json();

            this.setState({fullMembers: response_body, loading: false});
        } catch (error) {
            this.setState({loading: false});
            console.log(error);
        }
    }

    async checkConnection() {
        try {
            let response = await fetch('https://' + this.state.appHost + '/default_price/normal_beer',
                {
                    headers: {
                        'authentication-token': this.state.appToken
                    }
                });

            this.setState({connected: true});

        } catch (error) {
            this.setState({connected: false, loading: false});
            this.props.navigation.navigate("Parameters");
        }
    }

    async editMemberBalance(item, balance) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/transactions',
                    {
                        method: "post",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        },
                        body: JSON.stringify({
                            "member_id": item.id,
                            "party_id": null,
                            "label": "Mise à jour de balance manuelle",
                            "amount": parseFloat(balance) - parseFloat(item.balance)
                        })
                    });

                this.initiateMembers();
                Toast.show("Balance mise à jour !");
            }
        } catch (error) {
            console.log(error);
            this.setState({loading: false});
        }
    }

    async createAccount(first_name, last_name, phone, school, email) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                console.log(first_name);

                let response = await fetch('https://' + this.state.appHost + '/members',
                    {
                        method: "post",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        },
                        body: JSON.stringify({
                            "first_name": first_name,
                            "last_name": last_name,
                            "school": school,
                            "email": email,
                            "phone": phone
                        })
                    });

                this.initiateMembers();
                Toast.show("Utilisateur créé !");
                this.setState({
                    newMemberFirstName: "",
                    newMemberLastName: "",
                    newMemberEmail: "",
                    newMemberSchool: "",
                    newMemberPhone: ""
                })
            }
        } catch (error) {
            console.log(error);
            this.setState({loading: false});
        }
    }

    async closeAccount(memberId) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/members/' + memberId,
                    {
                        method: "delete",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        }
                    });

                this.initiateMembers();
                Toast.show("Compte clôturé !");
            }
        } catch (error) {
            console.log(error);
            this.setState({loading: false});
        }
    }

    async updateMembership(memberId) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/members/' + memberId + '/membership',
                    {
                        method: "put",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        }
                    });

                this.initiateMembers();
                Toast.show("Cotisation renouvelée !");
            }
        } catch (error) {
            console.log(error);
            this.setState({loading: false});
        }
    }

    async editMember(member_id, first_name, last_name, school, email, phone) {
        await this.checkConnection();

        try {
            if (this.state.connected) {
                let response = await fetch('https://' + this.state.appHost + '/members/' + member_id,
                    {
                        method: "put",
                        headers: {
                            'content-type': "application/json",
                            'authentication-token': this.state.appToken
                        },
                        body: JSON.stringify({
                            "first_name": first_name,
                            "last_name": last_name,
                            "school": school,
                            "email": email,
                            "phone": phone
                        })
                    });

                this.initiateMembers();
            }
        } catch (error) {
            console.log(error);
            this.setState({loading: false});
        }
    }

    preg(chaine) {
        let tab = {
            "é": "e", "è": "e", "ê": "e", "ë": "e",
            "ç": "c",
            "à": "a", "â": "a", "ä": "a",
            "î": "i", "ï": "i",
            "ù": "u",
            "ô": "o", "ó": "o", "ö": "o",
            " ": "", "'": "", "-": ""
        };

        for (i in tab) {
            chaine = chaine.toLowerCase().replace(new RegExp(i, "gi"), tab[i])
        }
        return chaine;
    }

    compareLastName(a, b) {
        if (a.last_name < b.last_name)
            return -1;
        if (a.last_name > b.last_name)
            return 1;
        return 0;
    }

    compareBalance(a, b) {
        if (a.balance < b.balance)
            return -1;
        if (a.balance > b.balance)
            return 1;
        return 0;
    }

    setPrice(text) {
        if (text === "" || text === "0")
            return "0";
        else
            return text.toString().replace(",", ".");
    }

    async exportMembers() {
        this.setState({loading: true});
        await this.getFullMembers();

        let selectedMembersIds = this.state.members.map(member => member.id)
        let selectedMembers = this.state.fullMembers.filter(value => selectedMembersIds.indexOf(value.id) !== -1)

        let csv = "Prénom,Nom,Solde,Ecole,Email,Téléphone,Dernière cotisation,Ancien staff ?\n"
        /* CSV formatting:
         *  - each value is double quoted to escape commas
         *  - each double quote in value is doubled to escape it
         */
        selectedMembers.forEach((m) => {
            csv += '"' + m.first_name + '","' + m.last_name + '","'
                + m.balance.toString().replace(/\./g, ',').replace(/ /g,'') + '","' + m.school + '","' + m.email + '","'
                + m.phone + '","' + m.last_membership_payment + '","' + (m.is_former_staff ? 1 : 0) + '"\n'
        })
        let path = RNFS.DocumentDirectoryPath + '/membres.csv';
        RNFS.writeFile(path, csv, 'ascii')
            .then((success) => {
                this.setState({loading: false});
                open({
                    title: 'Exporter les membres sélectionnés',
                    url: 'file://' + path,
                    type: 'text/csv',
                    showAppsToView: true,
                })
            })
            .catch((err) => {
                console.error(err.message);
                this.setState({loading: false});
            });
    }

    showMemberTransactions(item) {
        this.setState({
            viewPopup: false
        })
        this.props.navigation.navigate('MemberTransactions', {
            memberId: item.id,
            memberFirstName: item.first_name,
            memberLastName: item.last_name,
            refreshItems: (() => {
                this.initiateMembers()
            }).bind(this)
        })
    }

}