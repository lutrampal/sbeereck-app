import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    headerBar: {
        flexDirection: 'row', 
        height: 65,
        alignSelf: 'stretch',
        alignItems: 'center',
        backgroundColor: '#9d9d9d',
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 0,
    },
    textStyle: {
        color: '#FFF',
        fontSize: 20,
        fontWeight: 'bold',
        marginLeft: 15,
    },
    leftIconStyle: {
        marginTop: 3,
    },
    leftButtonStyle: {
        width: 50,
        height: 50,
        alignItems: 'center',
        justifyContent: 'center'
    },
    rightIconStyle: {
        marginTop: 3,
        marginRight: 15,
        alignSelf: 'flex-end'
    },
    rightButtonStyle: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center'
    },
});