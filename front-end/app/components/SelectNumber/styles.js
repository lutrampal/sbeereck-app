import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    container: {
        flexDirection: 'row',
        width: '100%',
        height: 'auto',
    },
    textStyle: {
        flex: 1,
        fontSize: 18,
        padding: 7,
        backgroundColor: '#eff0f1',
        borderRadius: 7,
        marginRight: 10,
        textAlign: 'center'
    },
    rightPart: {
        height: '100%',
        width: 'auto'
    },
    button: {
        width: 50,
        height: 30,
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 3,
        backgroundColor: '#edaf36',
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 0,
        marginBottom: 5,
    }
});