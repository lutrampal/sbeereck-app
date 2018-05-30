import React from 'react';
import { View, Text, FlatList, Keyboard, AsyncStorage, Alert, TouchableOpacity } from 'react-native';
import { NavigationActions } from 'react-navigation';
import EStyleSheet from 'react-native-extended-stylesheet';
import { Container } from '../components/Container';
import {Header} from '../components/Header';
import { Loading } from '../components/Loading';
import { Authentification } from '../components/Authentification';
import { AppParam } from '../components/AppParam';
import { SearchUser } from '../components/SearchUser';
import { SelectCategory } from '../components/SelectCategory';
import { TransactionBeer } from '../components/TransactionBeer';
import Icon from 'react-native-vector-icons/FontAwesome';
import { TransactionDeposit } from '../components/TransactionDeposit';
import { TransactionMoney } from '../components/TransactionMoney';
import { TransactionFood } from '../components/TransactionFood';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'

EStyleSheet.build({
    $mainBackground: '#F9F9F9'
})

export default class Home extends React.Component {
    constructor(props){
        super(props);

        this.state = {
            loading: false,
            connected: false,
            appHost: "",
            appToken: "",
            limitBalance: 0.0,
            partyId: this.props.navigation.state.params.partyId,
            normal_beer_price: 0.0,
            special_beer_price: 0.0,

            members: [],
            query: "",
            selectedMember: {},

            selectedCategory: 'beer',

            beers: [],
            selectedBeer: {},
            beersCount: 1,
            type: 1,

            deposits: [],
            selectedDeposit: {},
            depositsCount: 1,
            depositType: -1,

            foods: [],
            selectedFood: {},
            foodsCount: 1,

            moneyName: "Recharge",
            moneyCount: "0.00",
        }
    }

    findMember(query) {
        if(query === '')
            return [];

        const { members } = this.state;
        const regex = new RegExp(`${this.preg(query).trim()}`, 'i');
        return members.filter(member => { let string = this.preg(member.first_name + member.last_name); return string.search(regex) >= 0})
    }

    async componentDidMount() {
        this.setState({ loading: true });

        try {
            let host = await AsyncStorage.getItem('@SbeerEck:host');
            const token = await AsyncStorage.getItem('@SbeerEck:token');

            if (host != null) {
                host = host.replace(" ", "");
                this.setState({ appHost: host });
            }
            if (token != null)
                this.setState({ appToken: token });

            await this.checkConnection();

            if (this.state.connected) {
                response = await fetch('https://' + this.state.appHost + '/balance_too_low_threshold',
                    {
                        headers: {
                            'authentication-token': this.state.appToken
                        }
                    });

                request = await response.json();
                let balance_treshold = request.balance_too_low_threshold;

                response = await fetch('https://' + this.state.appHost + '/products?type=deposit',
                    {
                        headers: {
                            'authentication-token': this.state.appToken
                        }
                    });

                request = await response.json();
                let deposits = request;

                response = await fetch('https://' + this.state.appHost + '/products?type=food',
                    {
                        headers: {
                            'authentication-token': this.state.appToken
                        }
                    });

                request = await response.json();
                let foods = request;

                response = await fetch('https://' + this.state.appHost + '/parties/' + this.state.partyId,
                    {
                        headers: {
                            'authentication-token': this.state.appToken
                        }
                    });

                request = await response.json();
                let normal_beer_price = parseFloat(request.normal_beer_price);
                let special_beer_price = parseFloat(request.special_beer_price);
                let beers = request.served_beers;


                this.setState({ foods: foods, limitBalance: balance_treshold, beers: beers, normal_beer_price: normal_beer_price, special_beer_price: special_beer_price, deposits: deposits });

                this.initiateMembers();
            }
        } catch (error) {
            alert("Erreur lors du chargement des paramètres.\n" + error);
            this.setState({ loading: false });
        }
    }

    static navigationOptions = {
        disableGestures: true,
        drawerLockMode: 'locked-closed',
    }

    render() {
        const { query } = this.state;
        const members = this.findMember(query);
        const comp = (a, b) => a.toLowerCase().trim() === b.toLowerCase().trim();
        return(
            <Container>
                <Header
                    title="Nouvelle transaction"
                    leftButtonIcon="arrow-left"
                    leftButtonAction={() => { this.props.navigation.goBack(); Keyboard.dismiss()}}
                    rightButtonAction={this.saveParameters.bind(this)}
                    rightButtonIcon="check"
                />
                {this.showContent(members, query, comp)}
                <Loading shown={this.state.loading} />
            </Container>
        );
    }

    showContent(members, query, comp) {
        if(this.state.selectedMember.id == undefined)
            return (
                <KeyboardAwareScrollView style={{flex: 1, height: '100%'}} alwaysBounceVertical={false}>
                    <SearchUser
                        data={members.length === 1 && comp(this.preg(query), this.preg(members[0].first_name + members[0].last_name)) ? [] : members}
                        defaultValue={query}
                        onChangeText={text => this.setState({ query: text, selectedMember: {} })}
                        placeHolder="Entrez le nom du membre"
                        onItemPress={(item) => {
                            this.setState({ selectedMember: item })
                            this.setState({ query: item.first_name + " " + item.last_name })
                        }}
                        selectedMember={this.state.selectedMember}
                        limitBalance={this.state.limitBalance}
                    />
                </KeyboardAwareScrollView>
            )
        else
            return (
                <KeyboardAwareScrollView alwaysBounceVertical={false}>
                    <View style={{flexDirection: 'row', alignItems: 'center', padding: 10}}>
                        <TouchableOpacity onPress={() => { this.setState({ selectedMember: {}, query: ""})}}>
                            <Icon
                                name="times-circle"
                                size={20}
                                color="#c70100"
                            />
                        </TouchableOpacity>
                        <Text style={{ flex: 1, marginLeft: 5, height: 'auto', fontSize: 20, fontWeight: 'bold' }}>
                            {this.state.selectedMember.first_name} {this.state.selectedMember.last_name}
                        </Text>
                        {this.getUserBalance()}
                    </View>

                    <SelectCategory
                        selectedCategory={this.state.selectedCategory}
                        changeSelectedCategory={(item) => { this.setState({ selectedCategory: item }) }}
                    />

                    {this.openCategory()}
                </KeyboardAwareScrollView>
            )
    }

    getUserBalance() {
        if (this.state.selectedMember != null && this.state.selectedMember.id != undefined) {
            return (
                <View style={{ alignItems: 'center', width: "auto", height: "auto", padding: 5 }}>
                    <Text style={{ fontSize: 22 }}>Solde</Text>
                    {this.balance(this.state.selectedMember.balance)}
                </View>
            )
        }
    }

    balance(balance) {
        return (
            <Text style={[{ fontSize: 22 }, { color: this.balance_color(balance) }]}>{parseFloat(balance).toFixed(2).toString()}€</Text>
        )
    }

    balance_color(balance) {
        // according to the balance
        if (parseFloat(balance) <= parseFloat(this.state.limitBalance))
            return '#ba3f36';
        else
            return '#31978b';
    }

    openCategory() {
        switch (this.state.selectedCategory) {
            case 'beer':
                return (<TransactionBeer
                    normal_beer_price={this.state.normal_beer_price}
                    special_beer_price={this.state.special_beer_price}

                    beers={this.state.beers}
                    setSelectedBeer={(beer) => {this.setState({selectedBeer: beer})}}
                    selectedBeer={this.state.selectedBeer}

                    beersCount={this.state.beersCount}
                    setBeersCount={(count) => { this.setState({ beersCount: parseInt(count) }) }}
                    type={this.state.type}
                    changeSelectedType={(count) => { this.setState({ type: parseInt(count) }) }}
                />)
            case 'deposit':
                return (<TransactionDeposit
                    deposits={this.state.deposits}
                    setSelectedDeposit={(deposit) => { this.setState({ selectedDeposit: deposit }) }}
                    selectedDeposit={this.state.selectedDeposit}

                    depositsCount={this.state.depositsCount}
                    setDepositsCount={(count) => { this.setState({ depositsCount: parseInt(count) }) }}
                    depositType={this.state.depositType}
                    changeSelectedType={(count) => { this.setState({ depositType: parseInt(count) }) }}
                />)
            case 'food':
                return (<TransactionFood
                    foods={this.state.foods}
                    setSelectedFood={(food) => { this.setState({ selectedFood: food }) }}
                    selectedFood={this.state.selectedFood}

                    foodsCount={this.state.foodsCount}
                    setFoodsCount={(count) => { this.setState({ foodsCount: parseInt(count) }) }}
                />)
            case 'money':
                return (<TransactionMoney
                    setMoneyName={(money) => { this.setState({ moneyName: money }) }}
                    moneyName={this.state.moneyName}

                    moneyCount={this.state.moneyCount}
                    setMoneyCount={(count) => { this.setState({ moneyCount: count }) }}
                />)
                break;
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
            this.setState({ members: request, loading: false });
        } catch (error) {
            this.setState({ loading: false });
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

            this.setState({ connected: true });

        } catch (error) {
            this.setState({ connected: false, loading: false });
            this.props.navigation.navigate("Parameters");
        }
    }

    saveParameters() {
        if (this.state.selectedMember.id === undefined) {
            alert("Veuillez sélectionner un membre !");
            return null;
        }

        if (parseFloat(this.state.selectedMember.balance) < parseFloat(this.state.limitBalance) && (this.state.selectedCategory != 'money' && (this.state.selectedCategory != 'deposit' || this.state.depositType == -1)) ) {
            Alert.alert(
                'Balance dépassée',
                'Le compte du membre est en dessous de la limite de ' + this.state.limitBalance + ' euros. \nVoulez-vous forcer l\'opération?',
                [
                    { text: 'Annuler', onPress: () => { this.setState({ viewPopup: false }) }, style: 'cancel' },
                    {
                        text: 'Forcer', onPress: () => {
                            this.saveParameters2()
                        }
                    },
                ],
                { cancelable: false }
            )
        } else {
            this.saveParameters2();
        }
    }

    saveParameters2() {
        if (this.getMembership() == false) {
            Alert.alert(
                'Cotisation expirée',
                'La cotisation de ce membre est expirée. Sauf cas exceptionnel, l\'adhérent doit obligatoirement la renouveler avant de pouvoir procéder à un paiement. \nVoulez-vous tout de même forcer l\'opération?',
                [
                    { text: 'Annuler', onPress: () => { this.setState({ viewPopup: false }) }, style: 'cancel' },
                    {
                        text: 'Forcer', onPress: () => {
                            this.continueSaveParameters()
                        }
                    },
                ],
                { cancelable: false }
            )
        } else {
            this.continueSaveParameters();
        }
    }

    getMembership() {
        let textDate = this.state.selectedMember.last_membership_payment.replace(" ", "T");
        let dateMembership = new Date(textDate);
        dateMembership.setFullYear(dateMembership.getFullYear() + 1);

        let dateNow = new Date();

        var dd = dateMembership.getDate();
        var mm = dateMembership.getMonth() + 1;
        var yyyy = dateMembership.getFullYear();
        if (dd < 10) {
            dd = '0' + dd;
        }
        if (mm < 10) {
            mm = '0' + mm;
        }
        formatedDate = dd + '/' + mm + '/' + yyyy;

        if (this.state.selectedMember.is_former_staff)
            return true;
        if (dateMembership.getTime() <= dateNow.getTime())
            return false;
        return true;
    }

    async continueSaveParameters() {
        let bill = 0;
        let text = "";

        switch(this.state.selectedCategory) {
            case 'beer':
                if(this.state.selectedBeer.id === undefined) {
                    alert("Veuillez sélectionner une bière !");
                    return null;
                }
                bill = parseInt(this.state.beersCount, 10)*this.getBeerPrice()*parseInt(this.state.type, 10)*-1;
                text = this.state.beersCount + " " + this.getBeerType(this.state.type) + " de "  + this.state.selectedBeer.name;
                this.setState({loading: true});
                await this.createTransaction(bill, text);
                this.setState({loading: false});
                this.props.navigation.goBack();
                break;
            case 'deposit':
                if (this.state.selectedDeposit.id === undefined) {
                    alert("Veuillez sélectionner un type de caution !");
                    return null;
                }
                bill = parseInt(this.state.depositsCount, 10) * parseFloat(this.state.selectedDeposit.price) * parseInt(this.state.depositType, 10);
                text = this.state.depositsCount + " " + this.state.selectedDeposit.name + " " + this.getDepositType(this.state.depositType);
                this.setState({ loading: true });
                await this.createTransaction(bill, text);
                this.setState({ loading: false });
                this.props.navigation.goBack();
                break;
            case 'food':
                if (this.state.selectedFood.id === undefined) {
                    alert("Veuillez sélectionner un alliment !");
                    return null;
                }
                bill = parseInt(this.state.foodsCount, 10) * parseFloat(this.state.selectedFood.price) * -1;
                text = this.state.foodsCount + " " + this.state.selectedFood.name;
                this.setState({ loading: true });
                await this.createTransaction(bill, text);
                this.setState({ loading: false });
                this.props.navigation.goBack();
                break;
            case 'money':
                if (this.state.moneyName == "") {
                    alert("Veuillez entrer une description de transaction !");
                    return null;
                }
                bill = parseFloat(this.state.moneyCount);
                text = this.state.moneyName;
                this.setState({ loading: true });
                await this.createTransaction(bill, text);
                this.setState({ loading: false });
                this.props.navigation.goBack();
                break;
            default:
                alert("not implemented yet");
        }
    }

    async createTransaction(bill, text) {
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
                            "member_id": this.state.selectedMember.id,
                            "party_id": this.state.partyId,
                            "label": text,
                            "amount": bill,
                        })
                    });

                this.props.navigation.state.params.refreshItems();
                alert("Transaction créée !");
            }
        } catch (error) {
            console.log(error);
            this.setState({ loading: false });
        }
    }

    getBeerType(type) {
        switch (type) {
            case 1:
                return "demi(s)";
            case 2:
                return "pinte(s)";
            case 5:
                return "pichet(s)";
        }
    }

    getDepositType(type) {
        switch (type) {
            case -1:
                return "encaisée(s)";
            case 1:
                return "rendue(s)";
        }
    }

    getBeerPrice() {
        switch(this.state.selectedBeer.category) {
            case 'special':
                return parseFloat(this.state.special_beer_price);
            case 'normal':
                return parseFloat(this.state.normal_beer_price);
        }
    }

    setPrice(text) {
        if (text == "")
            return parseFloat("0");
        else
            return parseFloat(text.toString().replace(",", "."));
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
}
